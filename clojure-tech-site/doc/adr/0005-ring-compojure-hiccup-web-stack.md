# 5. Ring/Compojure/Hiccup Web Stack

Date: 2025-06-11

## Status

Accepted

Superseded by [11. Remove dynamic clojure site](0011-remove-dynamic-clojure-site.md)

## Context

Need to build a web application with static pages and dynamic blog content integration.

## Decision

Use Ring for HTTP handling, Compojure for routing, and Hiccup for HTML generation.

## Consequences
- **Positive:** Leverages Clojure's strengths, functional approach to web development, excellent composability
- **Negative:** Less mainstream than other web frameworks, requires Clojure expertise
- **Technical:** Clean separation of concerns with dedicated namespaces for pages, routes, and core functionality
