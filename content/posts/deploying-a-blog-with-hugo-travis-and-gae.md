---
title: "Deploying a blog with Hugo, TravisCI, and Google App Engine"
date: 2017-10-31T00:02:19-07:00
tags: [cd,shaving-yaks,tutorial,hugo,travis,google]
draft: true
showDate: true
---

I recently moved this site from being a couple of static files to using [Hugo](https://gohugo.io/) to create a generated static site with a blog, served with [Google App Engine](https://cloud.google.com/appengine/). I hit a few snags trying to automate this site with [TravisCI](https://travis-ci.org/) and Google App Engine, so I thought I'd share my process.

## Build a Hugo app, make it work locally
I'll handwave over this a bit. I've found Hugo to have a bit of a learning curve, but that's out of scope for this post. However, assuming you have a Hugo site that looks like you want it to when you serve with `hugo server`, then you're ready to deploy.

## Integrate with TravisCI
Make your Github repo public. (You have to pay for private repos on Travis. If you care about that, you can probably integrate with mostly the same steps. Look at TravisCI docs for how to set up for a private repo)

Sign in to travisci.org with your Github account and enable Travis for your repo.

Write a no-op `Makefile` in the root dir of your git repository.

```Makefile
run:
	echo "no-op makefile for travis"
```

Add a basic, empty golang .travis.yml file like so:

```yaml
language: go
go: 1.9
```

Commit and push your changes to Github. Travis should kick off a new build automatically and you should see your `no-op makefile for travis` print out in the Travis run, which should go green. You now have a basic test and deployment pipeline set up, even if it does nothing. Every time you push your commits to Github, your pipeline will run.

## Go get Hugo and run Hugo in your Travis file
Add the following to your .travis.yml to get and build Hugo, following the Hugo [installation docs](https://gohugo.io/getting-started/installing/#quick-install) for installing from source:
```yaml
before_install:
- go get github.com/kardianos/govendor
- govendor get github.com/gohugoio/hugo
- go install github.com/gohugoio/hugo
```

Update your Makefile to run Hugo in verbose mode. For good measure, add a clean step too.

```Makefile
run:
	hugo --verbose
clean:
	rm -rf public
```

The clean step will allow you to run `make clean` and remove your local generated site. Hugo doesn't always regenerate files properly, so you want to run against an empty public folder.

Try running `make` from the root dir of your repo. You'll see that hugo generates a public dir with all your static files.

## Set up Google App Engine:
Follow [these docs to make a google project](https://cloud.google.com/resource-manager/docs/creating-managing-projects).

Get the [gcloud cli here](https://cloud.google.com/sdk/docs/#install_the_latest_cloud_tools_version_cloudsdk_current_version) for testing (Optional. You could just keep pushing to travis but that's a slower feedback loop)

Write an app.yaml file in your root repo dir like so:
```yaml 
---
runtime: python27 # you don't care the runtime for static files. Python27 was easy
api_version: 1
threadsafe: true

handlers:
- url: /$
  static_files: public/index.html
  upload: public/index.html
- url: /posts # serve the list of posts
  static_files: public/posts/index.html
  upload: public/posts/index.html
- url: /posts/(.*)/ # serve each post
  static_files: public/posts/\1/index.html
  upload: public/posts/(.*)
- url: /(.*) # serve my css
  static_files: public/\1
  upload: public/(.*)
```

### What is the app.yaml doing?
Primarily you're setting up handlers to server your content. The url is the endpoint covered by that handler. The static_files say what static file to serve from the files uploaded on the server. The upload_files indicates what files to upload.

The regex capture groups `(.*)` in the url are then inserted in the places you see `\1` in the static_files section.

There's probably a more efficient, general way to set up your handlers. However, I had a very hard time getting the posts themself to serve, and this does work.

Try deploying your app with `gcloud app deploy`.
Check that all your pages deploy successfully, not just the index.

## Deploy to Google App Engine with Travis
[Travis's docs](https://docs.travis-ci.com/user/deployment/google-app-engine/) on deploying to Google App Engine are pretty good. Be sure to only check in the encrypted key, not the unencrypted key!!

I did have some problems getting Travis to decrypt the key. This is what ultimately ended up working for me:

```yaml
before_install:
- openssl aes-256-cbc -K $encrypted_6b8ed8e8b3dc_key -iv $encrypted_6b8ed8e8b3dc_iv
  -in travis-ci-account-key.json.enc -out travis-ci-service-account-key.json
  -d
```

The travis.yml deploy section looks like this:

```yaml
deploy:
  provider: gae
  keyfile: travis-ci-service-account-key.json
  project: <project-name>
  skip_cleanup: true
```

## Tada!

Your app should be now running on Google App Engine, and should be redeployed by TravisCI every time you push new commits to Github.

I highly recommend you set up https for your blog. To do that, you can either use lets encrypt or the [Google App Engine SSL beta](https://cloud.google.com/appengine/docs/standard/python/securing-custom-domains-with-ssl) (which is what I chose).

Once you have an SSL certificate set up, go to your `app.yaml` file, and for each route, add `secure: always`. This will redirect http traffic to https.

## Why did I choose this tooling stack?

I'm an engineer on Cloud Foundry. `cf push my-app` is what we build. Why isn't this a tutorial on "publishing a Hugo blog with ConcourseCi and Pivotal Web Services (Pivotal's hosted Cloud Foundry)"?

The answer is twofold. First, I was curious about Google App Engine, and how it compares, if it does, to pushing an app with `cf push`. The answer is, for a low-traffic static "app" like this blog, I don't think it much matters. At Cloud Foundry, we're much more focused on the experience of operators at large companies, and their developers. For a few static files, if you don't care who operates your platform, I don't see any relevant difference.

So then we get to the second answer: Price. I have an employee Pivotal Web Services account, but you probably don't. And for a single instance of a tiny, static blog like this with moderate traffic, Google App Engine's free tier can't be beat. PWS isn't competing for the individual developer market, and we shouldn't. Our goal with PCF is to transform how the industry writes (and deploys) software. Our customers are enterprise customers.

As for why Travis CI over ConcourseCI? Again, cost. I love concourse ci when you have complex build, test, and deploy pipelines. The idea of operating a concourse deployment just for this tiny site filled me with dread, however. It would be wildly cost in-efficient.

Why did I use Google App Engine over hosting the static files in Google Cloud Storage? Simply put, https. You can't use https to serve a CloudStorage website, and I beleive the whole web should be moving towards https.

If this site ends up with enough traffic to need to move past the free tier on Google App Engine someday, maybe you'll see another tutorial on how to port it elsewhere. For now, this stack is cheap (I pay only for my domain name) and fairly easy.


tl:dr I was curious and it was cheap.