package queue.systems;

import queue.Utils;

import java.util.HashMap;

/**
 * Created by Igor on 05.01.2017.
 */
public class OpenNetworkCalculator implements CalculatorFactory {

    private static HashMap<Class, Provider> providers = new HashMap<>();

    static {
        providers.put(ISSystem.class, Provider.ISOpenProvider());
        providers.put(LifoSystem.class, Provider.standardOpenProvider());
        providers.put(ProcesorSharingSystem.class, Provider.standardOpenProvider());
        providers.put(FifoSystem.class, Provider.fifoOpenProvider());
    }


    private abstract static class Provider {
        public abstract StateProbabilityCalculator createCalculator(QueueSystem system);

        public static Provider standardOpenProvider() {
            return new Provider() {
                @Override
                public StateProbabilityCalculator createCalculator(QueueSystem system) {
                    return new StateProbabilityCalculator(system) {

                        @Override
                        public double getProbability(int state) {
                            double result = (1 - system.getRho()) * (Math.pow(system.getRho(), state));
                            return result;
                        }
                    };
                }
            };
        }

        public static Provider ISOpenProvider() {
            return new Provider() {
                @Override
                public StateProbabilityCalculator createCalculator(QueueSystem system) {
                    return new StateProbabilityCalculator(system) {
                        @Override
                        public double getProbability(int state) {
                            return (Math.pow(Math.E, system.getRho())) * ((Math.pow(system.getRho(), state)) / (Utils.factorial(state)));
                        }
                    };
                }
            };
        }

        public static Provider fifoOpenProvider() {
            return new Provider() {
                @Override
                public StateProbabilityCalculator createCalculator(QueueSystem system) {
                    return new StateProbabilityCalculator(system) {

                        @Override
                        public double getProbability(int state) {

                            FifoSystem fifoSystem = (FifoSystem) system;
                            //m
                            int positionCount = fifoSystem.getM();

                            if (positionCount == 0) {
                                return (Provider.standardOpenProvider()).createCalculator(system).getProbability(state);
                            }

                            double zeroStateProbability = fifoSystem.getZeroStateProbability();
                            double result = 0;

                            ////6.27 equation from Wiley Interscience Queueing Networks
                            if (state <= fifoSystem.getM()) {
                                result = zeroStateProbability * (Math.pow(fifoSystem.getM() * fifoSystem.getRho(), state) / Utils.factorial(state));
                            } else {
                                result = zeroStateProbability * ((Math.pow(fifoSystem.getRho(), state) * Math.pow(fifoSystem.getM(), fifoSystem.getM())) / Utils.factorial(fifoSystem.getM()));
                            }

                            return result;
                        }
                    };
                }
            };
        }
    }

    @Override
    public StateProbabilityCalculator getCalculator(QueueSystem system) {
        Provider calculatorProvider = providers.get(system.getClass());
        if (calculatorProvider != null) {
            return calculatorProvider.createCalculator(system);
        }
        return null;
    }
}
