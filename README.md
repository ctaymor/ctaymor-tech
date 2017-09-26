Dependencies:
Runtime:
-
Dev:
* `sass`
* `gcloud sdk`
* `imagemagick`

To Build:

To watch sass files for changes, `sass --watch css/index.scss`

To Deploy:
```bash
cd ctaymor-home
gcloud app deploy --project ctaymor-home
```
