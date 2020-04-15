package uk.ac.ljmu.fet.cs.cloud.examples.autoscaler;

import java.util.ArrayList;
import java.util.Iterator;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine.State;

public class FourVMScaler extends VirtualInfrastructure {

	public FourVMScaler(IaaSService cloud) {
		super(cloud);
	}

	@Override
	public void tick(long fires) {
		// a list of all the applications that need a virtual infrastructure
		final Iterator<String> kinds = vmSetPerKind.keySet().iterator();
		while (kinds.hasNext()) {
			final String kind = kinds.next();
			final ArrayList<VirtualMachine> vmset = vmSetPerKind.get(kind);
			if (vmset.size() > 0) {
				if (vmset.size() < 1) {
					requestVM(kind);
				}
			} else if (vmset.isEmpty()) {
				requestVM(kind);
			} else
				for (VirtualMachine vm : vmset) {
					if (vm.getState().equals(State.RUNNING)) {
						// Destroys the virtual machine if it has no tasks or is empty
						if (vm.underProcessing.isEmpty() && vm.toBeAdded.isEmpty()) {
							destroyVM(vm);
						}
					}
				}
		}
	}
}
