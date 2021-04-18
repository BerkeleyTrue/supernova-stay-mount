(ns gopro-flare.core
  (:require [scad-clj.model :as m]
            [scad-tarmi.maybe :as maybe]))

(defn spline [spline-length]
  (let [sphere-r 0.7
        f-spine-length (- spline-length (* 2 sphere-r))]
    (maybe/translate
      [sphere-r 0 0]
      (m/hull
        (m/sphere sphere-r)
        (maybe/translate
          [f-spine-length 0 0]
          (m/sphere sphere-r))))))

(defn flare-mount []
  (let [flare-mount-height 16
        flare-mount-width 12.5
        flare-mount-thickness 2

        tab-extrusion-width 5.5
        tab-extrusion-height 10.3
        tab-extrusion-offset 10

        cut-out-thickness 1.68
        cut-out-depth 2.75
        chamfer 0.5
        spline-length 11.8]

    (maybe/union
      (m/minkowski
        (m/extrude-linear {:height (- flare-mount-thickness chamfer)}
          (maybe/difference
            ; main platform
            (maybe/union
              (m/square (- flare-mount-height chamfer) (- flare-mount-width chamfer))
              ; tab extrusion
              (maybe/translate
                [0
                 tab-extrusion-offset]
                (m/square tab-extrusion-width tab-extrusion-height)))
            ; cutouts
            (maybe/translate
              [(/ tab-extrusion-width 2)
               (- (/ flare-mount-width 2) cut-out-depth)]
              (m/square cut-out-thickness cut-out-depth :center false))
            (maybe/translate
              [(- (- 0 cut-out-thickness) (/ tab-extrusion-width 2))
               (- (/ flare-mount-width 2) cut-out-depth)]
              (m/square cut-out-thickness cut-out-depth :center false))))
        (m/sphere (/ chamfer 2)))
      ;; splines
      (let [spline-z (/ (- flare-mount-thickness chamfer) 2)
            spline-x (- (/ (- flare-mount-height spline-length) 2) (/ flare-mount-height 2))]

        (maybe/union
          (maybe/translate
            [spline-x
             1.76
             spline-z]
            (spline spline-length))

          (maybe/translate
            [spline-x
             (- (+ (/ flare-mount-width 4) 1))
             spline-z]
            (spline spline-length))))
      ; tab claw
      (maybe/translate
        [0
         (/ flare-mount-width 2)
         (- (/ flare-mount-thickness 2) 0.1)]
        (maybe/rotate
          [0 (m/deg->rad -90) 0]
          (m/extrude-linear
           {:height 5.5}
           (m/polygon [[0 0] [0 2.2] [1 2.2]]))))

      (let [tab-width 10
            tab-height 7.75
            camfer 0.3]

        ; tab head
        (maybe/translate
          [0
           (- (+ (/ tab-extrusion-height 2) tab-extrusion-offset) 1)
           (- (/ flare-mount-thickness 2) 0.5)]

          (maybe/rotate
            [(m/deg->rad -80) 0 0]
            (m/minkowski
              (maybe/union
                (m/extrude-linear
                  {:height 2.25}
                  (maybe/translate
                    [(- (/ tab-width 2)) 0 0]
                    (maybe/intersection
                      (m/square tab-width tab-height :center false)
                      (maybe/translate
                        [5 5]
                        (m/circle 5.5)))))
                ; tab thumb stop
                (maybe/translate
                  [0
                   (- tab-height 0.7)
                   (- (/ flare-mount-thickness 2) 0.1)]
                  (maybe/rotate
                    [(m/deg->rad -15)
                     0
                     0]
                    (m/extrude-linear
                      {:height 3.5}
                      (m/square 10 2.25)))))
              (m/sphere camfer))))))))

(defn main []
  [(m/fn! 50)
   (flare-mount)])
