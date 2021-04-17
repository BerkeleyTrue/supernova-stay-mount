(ns gopro-flare.core
  (:require [scad-clj.model :as m]
            [scad-tarmi.maybe :as maybe]))

(defn flare-mount[]
  (let [flareMountHeight 16
        flareMountWidth 12.5
        flareMountThickness 2

        tabExtrusionWidth 5.5
        tabExtrusionHeight 10.3
        cutOutThickness 1.68
        cutOutDepth 2.75
        chamfer 0.5]

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
      (m/sphere (/ chamfer 2)))))


(defn main []
  [(m/fn! 50)
   (flare-mount)])
