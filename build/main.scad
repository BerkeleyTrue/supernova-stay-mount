$fn = 50;
union () {
  minkowski () {
    linear_extrude (height=1.5, center=true){
      difference () {
        union () {
          square ([15.5, 12.0], center=true);
          translate ([0, 10, ]) {
            square ([5.5, 10.3], center=true);
          }
        }
        translate ([2.75, 3.5, ]) {
          square ([1.68, 2.75]);
        }
        translate ([-4.43, 3.5, ]) {
          square ([1.68, 2.75]);
        }
      }
    }
    sphere (r=0.25);
  }
  union () {
    translate ([-5.2, 1.76, 0.75]) {
      hull () {
        sphere (r=0.7);
        translate ([10.4, 0, 0]) {
          sphere (r=0.7);
        }
      }
    }
    translate ([-5.2, -3.125, 0.75]) {
      hull () {
        sphere (r=0.7);
        translate ([10.4, 0, 0]) {
          sphere (r=0.7);
        }
      }
    }
  }
}
