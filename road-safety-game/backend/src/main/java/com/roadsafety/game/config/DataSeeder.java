package com.roadsafety.game.config;

import com.roadsafety.game.model.AnswerOption;
import com.roadsafety.game.model.Scenario;
import com.roadsafety.game.repository.ScenarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Loads 10 starter scenarios the first time the app runs on an empty database. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ScenarioRepository scenarioRepository;

    public DataSeeder(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void run(String... args) {
        if (scenarioRepository.count() > 0) {
            return;
        }

        // ---------- Crossing ----------
        add("Crossing", "🚸", "At the Zebra Crossing",
                "You are walking to school and reach a zebra crossing. A few vehicles are coming. What do you do?",
                "Stop at the edge, look right, left, and right again. Cross only when vehicles have fully stopped.",
                opt("Run across quickly before the cars come", false,
                        "Running is risky. Drivers may not be able to stop in time."),
                opt("Stop, look right-left-right, and cross when vehicles have stopped", true,
                        "Great job! Looking carefully and waiting for vehicles to stop keeps you safe."),
                opt("Cross while looking at your phone", false,
                        "Phones take your eyes off the road. Always focus when crossing."));

        add("Crossing", "🛣️", "No Crossing Nearby",
                "You need to cross a busy road, but there is no zebra crossing or signal nearby.",
                "Choose a spot with a clear view in both directions, away from bends and parked vehicles.",
                opt("Step out from between two parked cars", false,
                        "Drivers cannot see you when you come out from behind parked cars."),
                opt("Walk to a spot with a clear view, then cross straight", true,
                        "Correct! A clear view lets you and the drivers see each other."),
                opt("Cross diagonally to save time", false,
                        "Crossing diagonally keeps you on the road longer. Always cross straight."));

        // ---------- Signals ----------
        add("Signals", "🚦", "Red Man Signal",
                "The pedestrian signal shows a red man, but the road looks empty.",
                "Red man means wait. Cross only when the green man shows.",
                opt("Cross quickly, no cars are here", false,
                        "A vehicle can appear suddenly. Always wait for the green man."),
                opt("Wait for the green man", true,
                        "Well done! Waiting for the green man is the safest choice."),
                opt("Follow the others who are crossing", false,
                        "Other people can make mistakes too. Follow the signal, not the crowd."));

        add("Signals", "🟡", "Yellow Light",
                "You are riding your cycle and the traffic light turns yellow as you get close.",
                "Yellow means get ready to stop. Slow down and stop safely behind the line.",
                opt("Pedal faster to beat the red light", false,
                        "Rushing through a signal is dangerous. Vehicles may start moving."),
                opt("Slow down and stop behind the line", true,
                        "Perfect! Yellow means prepare to stop."),
                opt("Ignore it and keep going", false,
                        "Traffic lights are there to keep everyone safe. Never ignore them."));

        // ---------- Cycling ----------
        add("Cycling", "🚴", "Helmet Time",
                "You are about to go cycling with your friends.",
                "Wear a properly fitted helmet on every ride, short or long.",
                opt("Wear a helmet and fasten the strap", true,
                        "Awesome! A helmet protects your head when you wear it properly."),
                opt("Helmets are only for long rides", false,
                        "Accidents can happen on short rides too. Wear a helmet every time."),
                opt("Hang the helmet on the handlebar", false,
                        "A helmet only protects you when it is on your head."));

        add("Cycling", "🌙", "Riding at Dusk",
                "It is getting dark and you are cycling home.",
                "Use lights and reflectors, wear bright clothes, and try to be home before dark.",
                opt("Wear dark clothes and ride fast to get home", false,
                        "Dark clothes make you hard to see, and speed makes it worse."),
                opt("Switch on lights, use reflectors, and wear bright clothes", true,
                        "Great! Lights and bright colours help drivers see you."),
                opt("Ride with no hands to look cool", false,
                        "Riding with no hands means you cannot brake or steer quickly."));

        // ---------- Bus ----------
        add("Bus", "🚌", "Getting Off the School Bus",
                "Your school bus has stopped and it is time to get off.",
                "Wait for the bus to stop completely, step down carefully, and let the bus leave before you cross.",
                opt("Jump off while it is still moving", false,
                        "Jumping off a moving bus can cause serious injuries."),
                opt("Wait until it stops, step down carefully, and let the bus leave before crossing", true,
                        "Excellent! Once the bus leaves, you can see the whole road."),
                opt("Run in front of the bus to cross", false,
                        "The driver cannot see you right in front of the bus."));

        add("Bus", "🚗", "Seat Belt Safety",
                "Your family is going for a drive in a car.",
                "Buckle up on every trip, and children are safest in the back seat.",
                opt("Buckle your seat belt every time", true,
                        "Super! Seat belts save lives on every trip."),
                opt("Skip the belt for short trips", false,
                        "Accidents can happen on short trips too. Always buckle up."),
                opt("Put your head out of the window to enjoy the breeze", false,
                        "Your head could hit something or you could be hurt in a sudden stop."));

        // ---------- Pedestrian ----------
        add("Pedestrian", "🚶", "Road With No Footpath",
                "You are walking along a road that has no footpath.",
                "Where traffic keeps left (like in India), walk on the right edge facing oncoming vehicles so you can see them coming.",
                opt("Walk in the middle of the road", false,
                        "The middle of the road is where vehicles drive. Stay at the edge."),
                opt("Walk on the right edge, facing oncoming vehicles", true,
                        "Right! You can see vehicles coming and step aside if needed."),
                opt("Walk on the left edge with your back to the traffic", false,
                        "You cannot see vehicles approaching from behind."));

        add("Pedestrian", "⚽", "Ball on the Road",
                "While you are playing, your ball rolls onto a busy road.",
                "Never chase a ball onto the road. Ask a grown-up for help, or wait until the road is clear.",
                opt("Run after it quickly", false,
                        "A ball can be replaced, you cannot. Never run onto the road."),
                opt("Stop, do not chase it, and ask a grown-up for help", true,
                        "Smart choice! Your safety matters more than the ball."),
                opt("Send a younger child to get it", false,
                        "That puts someone even smaller in danger. Ask a grown-up instead."));
    }

    private void add(String category, String emoji, String title, String description,
                     String tip, AnswerOption... options) {
        Scenario scenario = new Scenario();
        scenario.setCategory(category);
        scenario.setEmoji(emoji);
        scenario.setTitle(title);
        scenario.setDescription(description);
        scenario.setTip(tip);
        for (AnswerOption option : options) {
            option.setScenario(scenario);
            scenario.getOptions().add(option);
        }
        scenarioRepository.save(scenario);
    }

    private AnswerOption opt(String text, boolean correct, String feedback) {
        AnswerOption option = new AnswerOption();
        option.setOptionText(text);
        option.setCorrect(correct);
        option.setFeedback(feedback);
        return option;
    }
}
