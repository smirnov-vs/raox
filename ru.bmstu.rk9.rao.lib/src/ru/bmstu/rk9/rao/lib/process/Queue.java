package ru.bmstu.rk9.rao.lib.process;

import java.util.LinkedList;

import ru.bmstu.rk9.rao.lib.process.Process.BlockStatus;
import ru.bmstu.rk9.rao.lib.simulator.Simulator;

public class Queue implements Block {

	private InputDock inputDock = new InputDock();
	private OutputDock outputDock = new OutputDock();
	private java.util.Queue<Transact> queue = new LinkedList<Transact>();
	private int capacity;

	public Queue(int capacity) {
		this.capacity = capacity;
	}

	public InputDock getInputDock() {
		return inputDock;
	}

	public OutputDock getOutputDock() {
		return outputDock;
	}

	@Override
	public BlockStatus check() {
		Transact inputTransact = inputDock.pullTransact();
		if (inputTransact != null) {
			if (queue.size() < capacity) {
				queue.offer(inputTransact);
				System.out.println(Simulator.getTime() + " queue added " + inputTransact.getNumber());
				return BlockStatus.SUCCESS;
			} else
				return BlockStatus.CHECK_AGAIN;
		}

		Transact outputTransact = queue.peek();
		if (outputTransact != null) {
			if (!outputDock.hasTransact()) {
				queue.remove();
				System.out.println(Simulator.getTime() + " queue removed " + outputTransact.getNumber());
				outputDock.pushTransact(outputTransact);
				return BlockStatus.SUCCESS;
			}
		}
		return BlockStatus.NOTHING_TO_DO;
	}

}
