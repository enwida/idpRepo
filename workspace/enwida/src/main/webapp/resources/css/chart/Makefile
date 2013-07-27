ASSETS_DIR=assets
ASSETS=$(ASSETS_DIR)/bootstrap.css \
       $(ASSETS_DIR)/datepicker.css \
       $(ASSETS_DIR)/tipsy.css

compile_assets: $(ASSETS)
	cat $(ASSETS) | cleancss > assets.css
