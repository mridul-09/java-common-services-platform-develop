package com.leucine.services.jaas.constant;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class State {
  // PASSWORD_EXPIRED is only possible after the user has been VERIFIED. refer user.state.puml
  public static final Set<User> USER_VALID_REGISTERED_STATES = Collections.unmodifiableSet(EnumSet.of(User.REGISTERED, User.PASSWORD_EXPIRED));
  public static final List<Object> VALID_REGISTERED_STATES = List.of(User.REGISTERED.toString(), User.PASSWORD_EXPIRED.toString());

  public static final Set<User> USER_LOCKED_STATES = Collections.unmodifiableSet(EnumSet.of(User.REGISTERED_LOCKED, User.UNREGISTERED_LOCKED, User.ACCOUNT_LOCKED));

  private State() {
  }

  public enum User {
    REGISTERED,
    UNREGISTERED,
    INVITE_EXPIRED,
    INVITE_CANCELLED,
    UNREGISTERED_LOCKED,
    REGISTERED_LOCKED,
    ACCOUNT_LOCKED,
    PASSWORD_EXPIRED,
  }

  public enum License {
    INTIMATE,
    GRACE,
    GRACE_EXCEEDED,
    GENUINE,
    UNKNOWN
  }

}
