# 13. Use GCP Application Default Credentials for S3 access

Date: 2025-06-18

## Status

Accepted

## Context

Need secure GCS authentication for both local development and GitHub Actions CI/CD without storing credentials in source code.
### Alternatives considered
Service Account Key Files: Explicit but requires secret management and rotation
ADC with Dedicated Service Account: No secrets in code, automatic environment detection

## Decision

Use Google Cloud Application Default Credentials (ADC) with a dedicated minimal-privilege service account for GCS authentication in the blog RSS sync system.

## Consequences
### Mitigates risks:
Credential leakage (no files in repo)
Overprivileged access (minimal storage.objectAdmin permissions)
Key rotation overhead (Google manages)

### Accepted Risks
Requires gcloud CLI setup locally
GitHub Actions still needs service account key as secret
Less explicit than hardcoded credentials
