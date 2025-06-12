# 8. Separate RSS Sync from Build Process

Date: 2025-06-11

## Status

Accepted

Influences [9. Store synced RSS feed in S3 for reproducible builds](0009-store-synced-rss-feed-in-s3-for-reproducible-builds.md)

## Context

Site needs to sync content from external RSS feed, but build process should be fast and predictable.

## Decision

Use local JSON file for builds, separate workflow for RSS fetching that updates the local file and triggers rebuild.

## Consequences
**Positive**: Fast, predictable builds; clear separation of concerns; offline development capability
**Negative**: Slightly more complex automation (two workflows instead of one)
**Rationale**:: Better architecture than fetching RSS during every build; enables manual triggering and reduces external dependencies during build
