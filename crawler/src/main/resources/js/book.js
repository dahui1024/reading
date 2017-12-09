var page = require('webpage').create();

var system = require('system');
var args = system.args;
var url = args[1];

page.settings.loadImages=false;
page.settings.userAgent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";

page.open(url, function(status) {
  if(status !== "success") {
    phantom.exit();
  }
  setTimeout(function(){
    console.log(page.content);
    phantom.exit();
  }, 10000);
});
