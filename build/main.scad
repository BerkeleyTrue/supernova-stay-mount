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
  translate ([0, 6.25, 0.9]) {
    rotate ([0.0,-90.0,0.0]) {
      linear_extrude (height=5.5, center=true){
        polygon (points=[[0, 0], [0, 2.2], [1, 2.2]]);
      }
    }
  }
  translate ([0, 14.15, 0.5]) {
    rotate ([-80.0,0.0,0.0]) {
      minkowski () {
        union () {
          linear_extrude (height=2.25, center=true){
            translate ([-5, 0, 0]) {
              intersection () {
                square ([10, 7.75]);
                translate ([5, 5, ]) {
                  circle (r=5.5);
                }
              }
            }
          }
          translate ([0, 7.05, 0.9]) {
            rotate ([-14.999999999999998,0.0,0.0]) {
              linear_extrude (height=3.5, center=true){
                square ([10, 2.25], center=true);
              }
            }
          }
        }
        sphere (r=0.3);
      }
    }
  }
}
