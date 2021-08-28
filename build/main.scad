$fn = 50;
translate ([0, 0, 5/2]) {
  linear_extrude (height=5, center=true){
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
  }
}
