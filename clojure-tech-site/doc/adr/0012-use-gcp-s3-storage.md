# 12. Use GCP s3 storage

Date: 2025-06-18

## Status

Accepted

## Context

I plan to store the cached synced RSS files in s3, and then use those in the build process. I have already spent a lot of time on the website and want to get it set up. I also know I may switch s3 providers later - I'd rather use overseas owned services than American ones. But my main goal is to get this up, reasonably securely and functionally, fast. AWS seems to require up to 24 hours to set up an account and I already have a GCP account
## Decision
Use GCP s3 storage for the cached blog posts

## Consequences
Pro:
I can get started right now
It's where I've been hosting ctaymor-tech stuff for years so it makes sense

Con:
No good native clojure library
Will require Java Interop support
Still an ethically compromised hoster
Still an american owned, in trump's pocket service
