/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.checks;

import com.google.common.collect.ImmutableList;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.SyntaxTrivia;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import java.util.List;

/**
 * Note that {@link org.sonar.squidbridge.checks.AbstractNoSonarCheck} can't be used because of bug SSLRSQBR-16.
 */
@Rule(
  key = "NoSonar",
  name = "\"NOSONAR\" marker should not be used to switch-off issues",
  tags = {"bad-practice"},
  priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.API_ABUSE)
@SqaleConstantRemediation("1min")
public class NoSonarCheck extends SubscriptionBaseVisitor {

  private static final String PATTERN = "NOSONAR";
  private static final String MESSAGE = "Is //NOSONAR used to exclude false-positive or to hide real quality flaw ?";

  private final CommentContainsPatternChecker checker = new CommentContainsPatternChecker(this, PATTERN, MESSAGE);

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.TRIVIA);
  }

  @Override
  public void visitTrivia(SyntaxTrivia syntaxTrivia) {
    checker.checkTrivia(syntaxTrivia);
  }

}
