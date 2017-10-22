Dependencies:
Runtime:
* `minimal` hugo theme

Dev:
* `sass`
* `gcloud sdk`
* `imagemagick`
* `hugo`

To Build:

To watch sass files for changes, `sass --watch css/index.scss`

To Deploy:
```bash
cd ctaymor-home
gcloud app deploy --project ctaymor-home
```
