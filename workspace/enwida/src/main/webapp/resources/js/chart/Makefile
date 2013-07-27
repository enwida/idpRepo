ASSETS_DIR=assets
ASSETS=$(ASSETS_DIR)/jquery-1.9.1.min.js \
       $(ASSETS_DIR)/es5-sham.min.js \
       $(ASSETS_DIR)/es5-shim.min.js \
       $(ASSETS_DIR)/flight.js \
       $(ASSETS_DIR)/tipsy.js \
       $(ASSETS_DIR)/d3.v3.min.js \
       $(ASSETS_DIR)/d3-legend.js \
       $(ASSETS_DIR)/underscore-min.js \
       $(ASSETS_DIR)/bootstrap.min.js \
       $(ASSETS_DIR)/bootstrap-datepicker.js \
       $(ASSETS_DIR)/require.js

all: compile_assets compile_coffee compile_rjs

dev: compile_assets compile_coffee

compile_coffee: charts.coffee lib
	coffee -c .

compile_rjs: charts.js lib
	r.js -o baseUrl=lib name=../charts out=charts.js

compile_assets: $(ASSETS_DIR)
	uglifyjs $(ASSETS) > assets.js

clean:
	rm -f assets.js charts.js lib/*.js
