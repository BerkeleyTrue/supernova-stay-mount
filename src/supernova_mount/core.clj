(ns supernova-mount.core
  (:require [scad-clj.model :as m]
            [scad-tarmi.maybe :as maybe]))

(def sp-mount-width 11.5)

(defn sp-mount-side []
  (let [length (/ 61.4 2)
        pre-cut-length (- length (/ sp-mount-width 2))
        mount-hole-radius (/ 5 2)
        mounting-width (/ 50 2)]

    (maybe/difference
      (maybe/union
        (maybe/translate
          [0 (- (/ sp-mount-width 2))]
          (m/square pre-cut-length sp-mount-width :center false))
        (maybe/translate
          [pre-cut-length 0]
          (m/circle (/ sp-mount-width 2))))
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

(defn reinforcement []
  (let [radius 20]
    (maybe/intersection
      (maybe/translate
        [0 -10.5]
        (m/square 30 10))
      (maybe/translate
        [0 (* radius 0.55)]
        (m/circle radius)))))

(defn sp-mount []
  (let [thickness 7]
    (maybe/translate
      [0 0 (/ thickness 2)]

      (m/extrude-linear
        {:height thickness}
        (maybe/union
          (maybe/difference
            (maybe/union
              (sp-mount-side)
              (maybe/mirror
                [1 0 0]
                (sp-mount-side)))
            (cord-cutout))
          (reinforcement))))))


(defn frame-mount []
  (let [mount-hole-radius (/ 5.25 2)
        frame-mounting-bose 10
        frame-mount-thick 5
        mount-height (- 30 (/ frame-mounting-bose 2))]

    (maybe/translate
      [10 (- (/ (+ sp-mount-width frame-mount-thick) 2))]
      (maybe/rotate
        [(m/deg->rad 90) 0 0]
        (m/extrude-linear
          {:height frame-mount-thick}
          (maybe/difference
            (maybe/union
              (m/square frame-mounting-bose mount-height :center false)
              (maybe/translate
                [(/ frame-mounting-bose 2) mount-height]
                (m/circle (/ frame-mounting-bose 2))))
            (maybe/translate
              [(/ frame-mounting-bose 2) mount-height]
              (m/circle mount-hole-radius))))))))

(defn body []
  (maybe/union
    (sp-mount)
    (frame-mount)))

(defn main []
  [(m/fn! 50)
   (body)])
