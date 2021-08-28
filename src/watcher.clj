(ns watcher
  (:require [hawk.core :as hawk]
            [scad-clj.scad :refer [write-scad]]
            [aero.core :refer [read-config]]
            [nrepl.server :as nrepl]
            [nrepl.cmdline :refer [save-port-file]]
            [cider.nrepl :as cider]
            [supernova-mount.core :refer [main]]))


(def paths (read-config "paths.edn"))

(defonce ^:private watcher (atom nil))
(defonce ^:private repl-server (atom nil))

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
  (->>
    (hawk/watch!
      [{:paths (:watch paths)
        :filter hawk/file?
        :handler
        (fn [ctx _]
          (build-scad)
          ctx)}])
    (reset! watcher)))

(defn stop-watcher []
  (when watcher
    (hawk/stop! @watcher)
    (reset! watcher nil)))

(defn -main []
  (build-scad)
  (start-nrepl)
  (start-watching))

#_(-main)
