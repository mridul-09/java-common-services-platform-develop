package com.leucine.services.jaas.constant;

/**
 * CRITICAL: A problem occurred that might lead to service disruption if corrective action is not taken immediately.
 * ERROR: The event source is still performing; however, corrective action is required to avoid service disruption.
 * WARNING: The event source experienced an occurrence that you should be aware of. Events of this severity do not cause service disruption, and immediate corrective action
 * might not be required.
 * INFORMATION: The event occurs when a new object is discovered, or when a user action is performed.
 * UNKNOWN:
 */
public enum Severity {
  CRITICAL,
  ERROR,
  WARNING,
  INFORMATION,
  UNKNOWN
}
