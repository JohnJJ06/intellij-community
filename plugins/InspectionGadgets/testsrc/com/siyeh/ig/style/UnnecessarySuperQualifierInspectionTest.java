/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siyeh.ig.style;

import com.siyeh.ig.LightJavaInspectionTestCase;
import org.jetbrains.annotations.NotNull;

/**
 * @author Bas Leijdekkers
 */
public class UnnecessarySuperQualifierInspectionTest extends LightJavaInspectionTestCase {

  public void testUnnecessarySuperQualifier() {
    doTest();
  }
  
  public void testIgnoreClarificationSuperQualifier() {
    UnnecessarySuperQualifierInspection inspection = getInspection();
    inspection.ignoreClarification = true;
    myFixture.enableInspections(inspection);
    doTest();
  }

  @NotNull
  @Override
  protected UnnecessarySuperQualifierInspection getInspection() {
    return new UnnecessarySuperQualifierInspection();
  }
}