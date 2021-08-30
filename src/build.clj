(ns build
  (:require [scad-clj.scad :refer [write-scad]]
            [aero.core :refer [read-config]]
            [supernova-mount.core :as d]))

(defn- now [] (. System (nanoTime)))

(def paths (read-config "paths.edn"))

(defn -main []
  (println "building scad file")
  (let [start (now)]
    (->>
      (d/main)
      (apply write-scad)
      (spit (:dest paths)))

    (println
      (str
        "Build scad completed in "
        (->
          (now)
          (- start)
          (double)
          (/ 1000000.0))
        " ms"))))
