# 9. Store synced RSS feed in S3 for reproducible builds
Date: 2025-06-12

## Status

Accepted

Influences [11. Remove dynamic clojure site](0011-remove-dynamic-clojure-site.md)

Dependency [8. Separate RSS Sync from Build Process](0008-separate-rss-sync-from-build-process.md)

## Context

Need automated RSS syncing with build reproducibility and offline capability. Two options: (1) separate sync workflow storing to S3, or (2) RSS fetching during build process.

## Decision

Use separate RSS sync workflow that stores posts to S3, with builds reading from storage rather than fetching RSS directly.

## Consequences

**Positive**: Build reproducibility (same storage = same output); offline builds possible; better scaling for 50-100+ posts; clear separation of concerns; enables historical post integration

**Negative**: Additional infrastructure complexity (S3 + GitHub Actions workflows); two workflows instead of one

**Rationale**: Build reproducibility is critical for debugging and deployments. RSS fetching during builds creates non-deterministic outputs and external dependencies that could cause build failures.
