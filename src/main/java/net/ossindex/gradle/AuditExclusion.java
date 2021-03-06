package net.ossindex.gradle;

import java.util.LinkedList;
import java.util.List;

import net.ossindex.common.PackageCoordinate;
import net.ossindex.common.PackageCoordinate.Builder;
import net.ossindex.common.filter.IVulnerabilityFilter;

public class AuditExclusion
{
  private String vid;

  private List<String> packages;

  public String getVid() {
    return vid;
  }

  public void setVid(final String vid) {
    this.vid = vid;
  }

  public List<String> getPackages() {
    return packages;
  }

  public void setPackages(final List<String> packages) {
    this.packages = packages;
  }

  /**
   * Add this exclusion to the filter
   */
  public void apply(final IVulnerabilityFilter filter) {
    List<PackageCoordinate> list = getPackageList();
    if (vid == null) {
      filter.ignorePackage(list);
    }
    else {
      filter.ignoreVulnerability(list, vid);
    }
  }

  private List<PackageCoordinate> getPackageList() {
    List<PackageCoordinate> list = new LinkedList<>();
    if (packages != null) {
      for (String def : packages) {
        PackageCoordinate pkg = buildPackage(def);
        if (pkg != null) {
          list.add(pkg);
        }
      }
    }
    return list;
  }

  private PackageCoordinate buildPackage(final String def) {
    String[] tokens = def.split("/");
    if (tokens.length >= 2) {
      String[] nameTokens = tokens[1].split(":");
      Builder builder = PackageCoordinate.newBuilder()
          .withFormat("maven")
          .withNamespace(tokens[0])
          .withName(nameTokens[0]);
      if (nameTokens.length > 1) {
        builder = builder.withVersion(nameTokens[1]);
      }
      return builder.build();
    }
    return null;
  }

  @Override
  public String toString() {
    return "AuditExclusion{" +
        "vid='" + vid + '\'' +
        ", packages=" + packages +
        '}';
  }

  public boolean hasPackage(final String name) {
    if (packages != null) {
      for (String def : packages) {
        if (def.startsWith(name)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean hasVid(final String myVid) {
    if (vid != null) {
      return vid.equals(myVid);
    }
    return false;
  }
}
