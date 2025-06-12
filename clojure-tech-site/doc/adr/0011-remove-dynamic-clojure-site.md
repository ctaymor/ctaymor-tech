# 11. Remove dynamic clojure site

Date: 2025-06-12

## Status

Accepted

Supersedes [5. Ring/Compojure/Hiccup Web Stack](0005-ring-compojure-hiccup-web-stack.md)

Dependency [](0006-use-static-site-generation-over-dynamic-server.md)

Dependency [9. Store synced RSS feed in S3 for reproducible builds](0009-store-synced-rss-feed-in-s3-for-reproducible-builds.md)

## Context

Originally, I used a ring/compojure/hiccup web stack. However, I later decided to deploy to prod with generated static pages served by a CDN.
The RSS syncing will be done on a cron job, and saved to s3, and the build job will fetch it from s3.

## Decision

I will remove the dynamic server code entirely. Code will be built to `/dist` and the `/dist` folder served.

## Consequences
Pros:
Less code to maintain.
Only one codepath (alternative being 2: dynamic and static)

Harder:
No live reload for local development. Need to rebuild and run server.
