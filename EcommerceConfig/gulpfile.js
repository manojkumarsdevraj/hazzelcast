var gulp = require("gulp");
var sass = require("gulp-sass");
var uglify = require("gulp-uglify");

var templatename = "ECOMMERCE_STD_TEMPLATE_V2";
var cssPath = "EcommerceRepository/WEB_THEMES.war/"+templatename+"/css";
var scssPath = "EcommerceRepository/WEB_THEMES.war/"+templatename+"/scss/**/*.scss";
var jspath = "EcommerceRepository/WEB_THEMES.war/"+templatename+"/js/*.js";
gulp.task("sass", function() {
  return gulp
    .src(scssPath)
    .pipe(sass({ outputStyle: "compressed" }).on("error", sass.logError))
    .pipe(gulp.dest(cssPath));
});

gulp.task("jsmin", function() {
  return gulp
    .src(jspath)
    .pipe(uglify())
    .pipe(gulp.dest(jspath+"/compressed"));
});
gulp.task("default", ["sass", "jsmin"]);
