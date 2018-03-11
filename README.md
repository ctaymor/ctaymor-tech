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

To build hugo sass files: `make sass`

To Serve locally:
`hugo serve`

Add a -D flag to include draft posts.

To Deploy (or just git push and travis will handle it):
```bash
cd ctaymor-home
gcloud app deploy --project ctaymor-home
```
