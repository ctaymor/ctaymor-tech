# 2. RSS Feed Integration for Blog Content

Date: 2025-06-11

## Status

Accepted

## Context

I have an established (though very small) newsletter on Write.as. I enjoy writing on Write.as and find it lightweight. Migrating subscribers from one place to another is not trivial.
However, I want to incorporate those blogposts into the new Clojure website without maintaining duplicate content or complex CMS integration.

## Decision

Implement RSS feed syndication from Write.as using Clojure's XML parsing capabilities with the `clojure.data.zip` library.

## Consequences
- **Positive:** Single source of truth for blog content, automatic content updates, leverages existing Write.as publishing workflow
- **Negative:** Dependency on external service availability, requires robust error handling for feed parsing
- **Technical:** Implemented `extract-posts-from-xml` function with defensive programming for missing fields

