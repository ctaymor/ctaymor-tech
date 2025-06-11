# 7. Use Netlify for Hosting

Date: 2025-06-11

## Status

Accepted

## Context

Need simple, low-cost hosting with easy deployment automation. Preference for simple tools and build packs.

## Decision

Deploy to Netlify instead of continuing with Google App Engine.

## Consequences

**Positive**: Generous free tier for low traffic, excellent GitHub integration, simple deployment, good developer experience
**Negative**: Vendor lock-in, could get expensive at scale
**Rationale**: Fits requirements for simple tools, free tier, and easy maintenance. Alternative options (Vercel, Cloudflare Pages, GitHub Pages) considered but Netlify chosen for best documentation and developer experience
