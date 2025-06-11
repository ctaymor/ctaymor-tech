
Date: 2025-06-11

## Status

Accepted

## Context

 Need to deploy a simple Clojure consulting website with minimal traffic (1-2 views/week) and low maintenance overhead.

## Decision

Generate static HTML files from Clojure code instead of running a dynamic Ring server.

## Consequences
**Positive**: Minimal attack surface, free hosting options, fast loading, simple deployment
**Negative**: Need separate RSS sync process, slightly more complex build pipeline
**Rationale**: With extremely low traffic and security concerns about malicious content, static files provide the best risk/maintenance balance
