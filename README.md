This is the code for tech.taymor.io. PRs not accepted, but feel free to use it for inspiration/learning. (All writing content is Copyright Caroline Taymor and may not be copied or reproduced without permission).

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
