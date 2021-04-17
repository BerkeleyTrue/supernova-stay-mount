(ns build
  (:require [scad-clj.scad :refer [write-scad]]
            [aero.core :refer [read-config]]
            [gopro-flare.core :as d]))

(def paths (read-config "paths.edn"))

(defn -main []
  (spit (:dest paths) (write-scad (d/main))))
