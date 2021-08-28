(ns supernova-mount.core
  (:require [scad-clj.model :as m]
            [scad-tarmi.maybe :as maybe]))



(defn sp-mount-side []
  (let [width 11.5
        length (/ 61.4 2)
        pre-cut-length (- length (/ width 2))
        mount-hole-radius (/ 5 2)
        mounting-width (/ 50 2)]

    (maybe/difference
      (maybe/union
        (maybe/translate
          [0 (- (/ width 2))]
          (m/square pre-cut-length width :center false))
        (maybe/translate
          [pre-cut-length 0]
          (m/circle (/ width 2))))
      (maybe/translate
        [mounting-width 0]
        (m/circle mount-hole-radius)))))

(defn cord-cutout []
  (let [radius (/ 11 2)]
    (maybe/union
      (m/circle radius)
      (maybe/translate
        [0 radius]
        (m/square (* radius 2) (* radius 2))))))


(defn sp-mount []
  (let [thickness 5]
    (maybe/translate
      [0 0 (/ thickness 2)]

      (m/extrude-linear
        {:height thickness}
        (maybe/difference
          (maybe/union
            (sp-mount-side)
            (maybe/mirror
              [1 0 0]
              (sp-mount-side)))
          (cord-cutout))))))



(defn body []
  (sp-mount))

(defn main []
  [(m/fn! 50)
   (body)])
