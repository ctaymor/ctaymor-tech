# 3. HTML Content Sanitization Strategy

Date: 2025-06-11

## Status

Accepted

## Context

RSS feed content contains HTML that needs to be safely rendered to prevent XSS vulnerabilities while preserving formatting.

## Decision

Use Hickory library to parse HTML and convert to Hiccup format, combined with regex-based sanitization to remove dangerous elements.

## Consequences
- **Positive:** Avoids `dangerouslySetInnerHTML` security risks, maintains content formatting, integrates well with Hiccup rendering
- **Negative:** Additional dependency, regex-based sanitization may need refinement for edge cases
- **Technical:** Implemented `html-to-hiccup` and `simply-sanitize` functions with fallback error handling

