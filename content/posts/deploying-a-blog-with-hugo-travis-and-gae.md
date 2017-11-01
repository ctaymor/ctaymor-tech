---
title: "Deploying a blog with Hugo, Travis CI, and Google App Engine"
date: 2017-10-31T00:02:19-07:00
tags: [cd,shaving-yaks,tutorial,hugo,travis,google]
draft: true
showDate: true
---

1. build a hugo app, make it work locally
Once you have a hugo page that looks like you want it to when you serve with hugo server, then you're ready to deploy.
2. Integrate with travis ci:
Make your github repo public. (You have to pay for private travis. If you care about that, you can probably integrate with mostly the same steps. Look at travis ci docs for how to set up for a private repo)

Sign in to travisci.org with your github account and enable travis for your repo.

Write a no-op Makefile:
```make
run:
	echo "no-op makefile for travis"
```

Add a basic, empty go travis file like so:

```yaml
language: go
go: 1.9
```

commit and push. You should see your `no-op makefile for travis` print out in the travis run, which should go green. You now have a basic test and deployment pipeline set up, even if it does nothing. Every time you push your commits to github, your pipeline will run.
3. Go get hugo and run hugo in your travis file
Add the following to your travis.yml to get and build hugo, following the hugo installation docs:
```yaml
before_install:
- go get github.com/kardianos/govendor
- govendor get github.com/gohugoio/hugo
- go install github.com/gohugoio/hugo
```

Update your makefile to run hugo in verbose mode.

```make
run:
	hugo --verbose
```

For good measure, add a clean step to your makefile:

```
clean:
	rm -rf public
```

this will allow you to run `make clean` and remove your local generated site. Hugo doesn't always regenerate files properly, so you want to run against an empty public folder.

5. Set up Google App Engine:
follow these docs to make a google project: https://cloud.google.com/resource-manager/docs/creating-managing-projects
and get the gcloud cli here for testing

Write an app.yaml file like so:
 (not sure how to do posts stuff yet in the yaml, so we'll come back to that when I get there.)
```yaml 


```
Try deploying your app with `gcloud app deploy`.
Check that all your pages deploy successfully, not just the index.

Travis's docs on deploying to google app engine are pretty good. Be sure to only check in the encrypted key, not the unencrypted key!!

I did have some problems getting travis to decrypt the key. This is what ultimately ended up working for me:

```yaml
before_install:
- openssl aes-256-cbc -K $encrypted_6b8ed8e8b3dc_key -iv $encrypted_6b8ed8e8b3dc_iv
  -in travis-ci-account-key.json.enc -out travis-ci-service-account-key.json
  -d
```


Now, you might be asking yourself why I wrote a tutorial for how to publish a hugo blog with travis ci and Google App Engine, when I'm an engineer on Cloudfoundry. Why isn't this "publish a hugo blog with concourse ci and Pivotal Web Services (Pivotal's hosted cloudfoundry)".
The answer is twofold. First, I was curious about Google App Engine, and how it compares, if it does, to pushing an app with `cf push`. The answer is, for a low-traffic static "app" like this blog, I don't think it much matters. A static site isn't where Cloudfoundry shines, although I'm sure it's good at a static site too. But why use a semi when you need a compact car?
So then we get to the second answer: Price. I have an employee Pivotal Web Services account, but you probably don't. And for a single instance of a tiny, static blog like this with moderate traffic, Google App Engine's free tier can't be beat. PWS isn't competing for that market, and we shouldn't. Our goal with PCF is to transform how the industry writes software. Our customers are enterprise customers. If this site ends up with enough traffic to need to move past the free tier on Google App Engine someday, maybe you'll see another tutorial on how to port it elsewhere. For now, a little single instance Google App Engine is free and easy.

And why did I use Google App Engine over hosting the static files in Google Cloud Storage? Simply put, https. You can't use https to serve a CloudStorage website, and I beleive the whole web should be moving towards https.
tl:dr I was bored and it was cheap.