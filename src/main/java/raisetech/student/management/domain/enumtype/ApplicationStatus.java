package raisetech.student.management.domain.enumtype;

public enum ApplicationStatus {
  TEMPORARY,   // 仮申込
  OFFICIAL,    // 本申込
  IN_PROGRESS, // 受講中
  COMPLETED,   // 受講終了
  UNKNOWN;     // 不正値・null用

  public static ApplicationStatus fromDb(String value) {
    if (value == null || value.isBlank()) {
      return UNKNOWN;
    }
    try {
      return ApplicationStatus.valueOf(value);
    } catch (IllegalArgumentException e) {
      return UNKNOWN;
    }
  }
}
