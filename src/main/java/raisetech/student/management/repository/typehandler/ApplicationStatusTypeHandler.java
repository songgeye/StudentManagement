package raisetech.student.management.repository.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import raisetech.student.management.domain.enumtype.ApplicationStatus;

@MappedTypes(ApplicationStatus.class)
public class ApplicationStatusTypeHandler extends BaseTypeHandler<ApplicationStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ApplicationStatus parameter,
      JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, parameter.name());
  }

  @Override
  public ApplicationStatus getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    return ApplicationStatus.fromDb(rs.getString(columnName));
  }

  @Override
  public ApplicationStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return ApplicationStatus.fromDb(rs.getString(columnIndex));
  }

  @Override
  public ApplicationStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return ApplicationStatus.fromDb(cs.getString(columnIndex));
  }
}