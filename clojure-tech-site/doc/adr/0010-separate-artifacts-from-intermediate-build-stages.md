# 10. Separate artifacts from intermediate build stages
Date: 2025-06-12

## Status

Accepted

## Context

Need clear separation between build inputs, intermediate artifacts, and final deployable output.

## Decision

Use separate directories: `build/` for intermediate artifacts (RSS sync output), `dist/` for final static site, `resources/` for committed fallback data.

## Consequences

**Positive**: Clear separation of concerns; aligns with standard web deployment patterns; easy Netlify configuration

**Negative**: Slightly more complex file structure than single build directory

**Rationale**: Matches common frontend tooling patterns and makes the build pipeline easier to understand and debug.
