(ns watcher
  (:require [clojure.core.async :refer [chan go-loop <! >! >!! timeout alts! close!]]
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

(defn debounce-fn [in-fn ms]
  (let [out (chan)
        in (chan)]

    ; consumer
    (go-loop []
      ; wait and take messages out
      (<! out)
      ; call func
      (in-fn)
      ; loop back and wait
      (recur))

    ; observer
    (go-loop [last-val nil]
      (let [val (if (nil? last-val)
                  ; if val is nil, wait for new input
                  (<! in)
                  last-val)
            ; new channel that closes in ms
            timer (timeout ms)
            ; alts races both channels
            ; which ever responds first wins
            ; loser is closed?
            [new-val ch] (alts! [in timer])]

        (condp = ch
          ; timer responded first
          ; no new inputs in ms window
          timer (do
                  ; call out to out channel
                  (when (>! out val)
                    ; when out is closed close in
                    (close! in))
                  ; clear previous value
                  (recur nil))
          ; new input
          ; update cached value and loop
          in (recur new-val))))
    ; return new function to be called
    #(>!! in %)))

(defn build-scad []
  (println "building...")
  (use 'supernova-mount.core :reload-all)
  (time
    (->>
      (main)
      (apply write-scad)
      (spit (:dest paths))))
  (println "done building!"))

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
  (let [on-change (debounce-fn build-scad 250)]
    (->>
      (hawk/watch!
        [{:paths (:watch paths)
          :filter hawk/modified?
          :handler
          (fn [ctx e]
            (on-change e)
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
