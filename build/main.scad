$fn = 50;
translate ([0, 0, 7/2]) {
  linear_extrude (height=7, center=true){
    union () {
      difference () {
        union () {
          difference () {
            union () {
              translate ([0, -5.75, ]) {
                square ([24.95, 11.5]);
              }
              translate ([24.95, 0, ]) {
                circle (r=5.75);
              }
            }
            translate ([25, 0, ]) {
              circle (r=5/2);
            }
          }
          mirror ([1, 0, 0]) {
            difference () {
              union () {
                translate ([0, -5.75, ]) {
                  square ([24.95, 11.5]);
                }
                translate ([24.95, 0, ]) {
                  circle (r=5.75);
                }
              }
              translate ([25, 0, ]) {
                circle (r=5/2);
              }
            }
          }
        }
        union () {
          circle (r=11/2);
          translate ([0, 11/2, ]) {
            square ([11, 11], center=true);
          }
        }
      }
      intersection () {
        translate ([0, -10.5, ]) {
          square ([30, 10], center=true);
        }
        translate ([0, 11.0, ]) {
          circle (r=20);
        }
      }
    }
  }
}
