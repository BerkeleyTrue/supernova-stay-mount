(ns gopro-flare.core
  (:require [scad-clj.model :as m]
            [scad-tarmi.maybe :as maybe]))

(defn spline [spine-length]
  (let [sphere-r 0.7
        f-spine-length (- spine-length (* 2 sphere-r))]
    (maybe/translate
      [sphere-r 0 0]
      (m/hull
        (m/sphere sphere-r)
        (maybe/translate
          [f-spine-length 0 0]
          (m/sphere sphere-r))))))

(defn flare-mount []
  (let [flareMountHeight 16
        flareMountWidth 12.5
        flareMountThickness 2

        tabExtrusionWidth 5.5
        tabExtrusionHeight 10.3
        cutOutThickness 1.68
        cutOutDepth 2.75
        chamfer 0.5
        spine-length 11.8]

    (maybe/union
      (m/minkowski
        (m/extrude-linear {:height (- flareMountThickness chamfer)}
          (maybe/difference
            ; main platform
            (maybe/union
              (m/square (- flareMountHeight chamfer) (- flareMountWidth chamfer))
              ; tab extrusion
              (maybe/translate
                [0
                 10]
                (m/square tabExtrusionWidth tabExtrusionHeight)))
            ; cutouts
            (maybe/translate
              [(/ tabExtrusionWidth 2)
               (- (/ flareMountWidth 2) cutOutDepth)]
              (m/square cutOutThickness cutOutDepth :center false))
            (maybe/translate
              [(- (- 0 cutOutThickness) (/ tabExtrusionWidth 2))
               (- (/ flareMountWidth 2) cutOutDepth)]
              (m/square cutOutThickness cutOutDepth :center false))))
        (m/sphere (/ chamfer 2)))
      ;; splines
      (let [spline-z (/ (- flareMountThickness chamfer) 2)
            spline-x (- (/ (- flareMountHeight spine-length) 2) (/ flareMountHeight 2))]

        (maybe/union
          (maybe/translate
            [spline-x
             1.76
             spline-z]
            (spline spine-length))

          (maybe/translate
            [spline-x
             (- (/ flareMountWidth 4))
             spline-z]
            (spline spine-length)))))))

(defn main []
  [(m/fn! 50)
   (flare-mount)])
