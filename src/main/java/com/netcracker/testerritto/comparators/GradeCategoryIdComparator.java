package com.netcracker.testerritto.comparators;

import com.netcracker.testerritto.models.GradeCategory;

import java.util.Comparator;

public class GradeCategoryIdComparator implements Comparator<GradeCategory> {

  @Override
  public int compare(GradeCategory o1, GradeCategory o2) {
    if (o1.getId() < o2.getId())
      return -1;
    else if (o1.getId() > o2.getId())
      return 1;
    return 0;
  }
}
