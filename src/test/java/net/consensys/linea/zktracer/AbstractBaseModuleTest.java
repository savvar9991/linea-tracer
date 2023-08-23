/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer;

import java.util.List;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.testutils.BytecodeCompiler;
import net.consensys.linea.zktracer.testutils.PureTestCodeExecutor;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Base test class used to set up mocking of a {@link MessageFrame}, {@link OpCode} and trace
 * generation functionality.
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractBaseModuleTest {
  MessageFrame mockFrame;
  Operation mockOperation;
  static Module module;

  @BeforeAll
  static void beforeAll() {
    OpCodes.load();
  }

  @BeforeEach
  void beforeEach() {
    module = getModuleTracer();
  }

  protected void runTest(final OpCodeData opCodeData, final List<Bytes32> arguments) {
    BytecodeCompiler bytecode = new BytecodeCompiler();
    for (Bytes32 argument : arguments) {
      bytecode.push(argument);
    }
    bytecode.op(opCodeData.mnemonic());

    new PureTestCodeExecutor(bytecode.compile()).run();
  }

  protected String traceTest(final OpCodeData opCodeData, final List<Bytes32> arguments) {
    BytecodeCompiler bytecode = new BytecodeCompiler();
    for (Bytes32 argument : arguments) {
      bytecode.push(argument);
    }
    bytecode.op(opCodeData.mnemonic());

    return new PureTestCodeExecutor(bytecode.compile()).trace();
  }

  protected abstract Module getModuleTracer();
}
