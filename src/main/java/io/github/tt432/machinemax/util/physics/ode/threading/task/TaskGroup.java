/*************************************************************************
 *                                                                       *
 * Open Dynamics Engine 4J                                               *
 * Copyright (C) 2017 Piotr Piastucki, Tilmann Zaeschke                  *
 * All rights reserved.  Email: ode4j@gmx.de   Web: www.ode4j.org        *
 *                                                                       *
 * This library is free software; you can redistribute it and/or         *
 * modify it under the terms of EITHER:                                  *
 *   (1) The GNU Lesser General Public License as published by the Free  *
 *       Software Foundation; either version 2.1 of the License, or (at  *
 *       your option) any later version. The text of the GNU Lesser      *
 *       General Public License is included with this library in the     *
 *       file LICENSE.TXT.                                               *
 *   (2) The BSD-style license that is included with this library in     *
 *       the file ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT.         *
 *                                                                       *
 * This library is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the files    *
 * LICENSE.TXT, ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT for more   *
 * details.                                                              *
 *                                                                       *
 *************************************************************************/
package io.github.tt432.machinemax.util.physics.ode.threading.task;

public class TaskGroup extends Task {

	TaskGroup(TaskExecutor executor, String name, Runnable runnable) {
		this(executor, name, null, runnable);
	}

	TaskGroup(TaskExecutor executor, String name, TaskGroup parent, Runnable runnable) {
		super(executor, name, parent, runnable);
		subtaskCount.incrementAndGet();
	}

	public TaskGroup subgroup(String name, Runnable runnable) {
		TaskGroup subgroup = new TaskGroup(executor, name, this, runnable);
		subtaskCount.incrementAndGet();
		return subgroup;
	}

	public Task subtask(String name, Runnable runnable) {
		Task subtask = new Task(executor, name, this, runnable);
		subtaskCount.incrementAndGet();
		return subtask;
	}

	@Override
	public void submit() {
		subtaskCompleted();
	}

	void subtaskCompleted() {
		if (subtaskCount.decrementAndGet() == 0) {
			super.submit();
		}
	}

}
