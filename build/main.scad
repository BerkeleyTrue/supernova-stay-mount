$fn = 50;
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
