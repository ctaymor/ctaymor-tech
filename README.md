This is the code for tech.taymor.io. PRs not accepted, but feel free to use it for inspiration/learning. (All writing content is Copyright Caroline Taymor and may not be copied or reproduced without permission).

-------
Builds:
[![Build Status](https://travis-ci.org/ctaymor/ctaymor-tech.svg?branch=master)](https://travis-ci.org/ctaymor/ctaymor-tech)
-------

Dependencies:
Runtime:
* `minimal` hugo theme

Dev:
* `sass`
* `gcloud sdk`
* `imagemagick`
* `hugo`

To Build:

To build sass files, `sass www/css/index.scss www/css/index.css`

To Deploy:
```bash
cd ctaymor-home
gcloud app deploy --project ctaymor-home
```
