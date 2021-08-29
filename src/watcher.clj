(ns watcher
  (:require [clojure.core.async :refer [chan go go-loop take! <! >! >!! timeout alts!]]
            [hawk.core :as hawk]
            [scad-clj.scad :refer [write-scad]]
            [aero.core :refer [read-config]]
            [nrepl.server :as nrepl]
            [nrepl.cmdline :refer [save-port-file]]
            [cider.nrepl :as cider]
            [supernova-mount.core :refer [main]]))


(def paths (read-config "paths.edn"))

(defonce ^:private watcher (atom nil))
(defonce ^:private repl-server (atom nil))

(defn debounce [in ms]
  (let [out (chan)]
    (go-loop [last-val nil]
      (let [val (if (nil? last-val)
                  (<! in)
                  last-val)
            timer (timeout ms)
            [new-val ch] (alts! [in timer])]
        (condp = ch
          timer (do
                  (>! out val)
                  (recur nil))
          in (recur new-val))))
    out))

(defn build-scad []
  (println "building")
  (use 'supernova-mount.core :reload-all)
  (->>
    (main)
    (apply write-scad)
    (spit (:dest paths))))

(defn start-nrepl []
  (->
    (nrepl/start-server
      :headless false
      :handler (apply nrepl/default-handler cider/cider-middleware))

    (save-port-file {})
    (->> (reset! repl-server))))

(defn stop-nrepl []
  (when repl-server
    (nrepl/stop-server @repl-server)
    (reset! repl-server nil)))

(defn start-watching []
  (let [in (chan)
        out (debounce in 250)]

    (go (take! out (fn [_] (build-scad))))
    (->>
      (hawk/watch!
        [{:paths (:watch paths)
          :filter hawk/modified?
          :handler
          (fn [ctx e]
            (>!! in e)
            ctx)}])
      (reset! watcher))))

(defn stop-watcher []
  (when watcher
    (hawk/stop! @watcher)
    (reset! watcher nil)))

(defn -main []
  (build-scad)
  (start-nrepl)
  (start-watching))

#_(-main)
