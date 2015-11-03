import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.awt.Dimension;

public class Recognizer {

    /** Number of points to use for the re-sampled path. */
    private static final int N = 32;
    private static ArrayList<Template> templates = new ArrayList<>();

    public static void initializeTemplates() {
        Gesture navigable = Gesture.NAVIGABLE;
        templates.add(new Template(navigable, new ArrayList<>(Arrays.asList(
            new Point(0, 50, 1), new Point(100, 50, 1), new Point(200, 50, 1),
            new Point(150, 0, 2), new Point(200, 50, 2), new Point(150, 100, 2)
        ))));

        Gesture classifier = Gesture.CLASSIFIER;
        templates.add(new Template(classifier, new ArrayList<>(Arrays.asList(
            new Point(0, 0, 1), new Point(0, 400, 1),
            new Point(0, 0, 2), new Point(200, 0, 2), new Point(200, 400, 2),
            new Point(0, 400, 3), new Point(200, 400, 3)
        ))));

        Gesture generalization = Gesture.GENERALIZATION;
        templates.add(new Template(generalization, new ArrayList<>(Arrays.asList(
            new Point(0, 50, 1), new Point(100, 50, 1), new Point(200, 50, 1),
            new Point(200, 0, 2), new Point(200, 100, 2),
            new Point(200, 0, 3), new Point(250, 50, 3), new Point(200, 100, 3)
        ))));

        Gesture unspecified = Gesture.UNSPECIFIED;
        templates.add(new Template(unspecified, new ArrayList<>(Arrays.asList(
            new Point(0, 50, 1), new Point(100, 50, 1), new Point(200, 50, 1)
        ))));

        Gesture binavgiable = Gesture.BI_NAVIGABLE;
        templates.add(new Template(binavgiable, new ArrayList<>(Arrays.asList(
            new Point(160,102,1), new Point(159,102,1), new Point(155,105,1), new Point(152,109,1), new Point(144,115,1), new Point(137,123,1), new Point(131,130,1), new Point(121,139,1), new Point(114,146,1), new Point(109,150,1), new Point(104,155,1), new Point(102,157,1), new Point(101,158,1), new Point(101,160,1), new Point(101,161,1), new Point(103,161,1), new Point(106,164,1), new Point(110,166,1), new Point(116,170,1), new Point(123,173,1), new Point(129,177,1), new Point(133,180,1), new Point(139,184,1), new Point(143,186,1), new Point(147,188,1), new Point(149,190,1), new Point(152,191,1), new Point(154,193,1), new Point(156,194,1), new Point(158,194,1), new Point(159,195,1), new Point(161,196,1), new Point(161,197,1), new Point(162,197,1), new Point(103,152,2), new Point(104,152,2), new Point(108,152,2), new Point(113,152,2), new Point(120,152,2), new Point(123,152,2), new Point(128,152,2), new Point(131,152,2), new Point(135,152,2), new Point(141,152,2), new Point(146,152,2), new Point(153,152,2), new Point(160,152,2), new Point(168,152,2), new Point(177,152,2), new Point(183,152,2), new Point(190,152,2), new Point(198,152,2), new Point(205,152,2), new Point(213,152,2), new Point(220,152,2), new Point(228,152,2), new Point(236,152,2), new Point(243,152,2), new Point(252,152,2), new Point(260,152,2), new Point(267,152,2), new Point(276,152,2), new Point(281,152,2), new Point(287,152,2), new Point(293,152,2), new Point(297,152,2), new Point(302,152,2), new Point(308,152,2), new Point(314,152,2), new Point(320,151,2), new Point(324,150,2), new Point(329,150,2), new Point(333,149,2), new Point(336,148,2), new Point(339,148,2), new Point(340,148,2), new Point(342,148,2), new Point(305,94,3), new Point(307,94,3), new Point(310,94,3), new Point(315,95,3), new Point(321,98,3), new Point(326,103,3), new Point(330,107,3), new Point(335,112,3), new Point(338,115,3), new Point(341,118,3), new Point(346,121,3), new Point(348,124,3), new Point(352,126,3), new Point(354,128,3), new Point(357,131,3), new Point(360,133,3), new Point(361,135,3), new Point(363,137,3), new Point(364,138,3), new Point(365,139,3), new Point(365,140,3), new Point(366,140,3), new Point(366,141,3), new Point(368,143,3), new Point(370,144,3), new Point(373,146,3), new Point(374,147,3), new Point(375,148,3), new Point(375,149,3), new Point(375,150,3), new Point(373,151,3), new Point(370,154,3), new Point(367,156,3), new Point(364,160,3), new Point(361,163,3), new Point(358,166,3), new Point(355,170,3), new Point(354,172,3), new Point(353,172,3), new Point(350,173,3), new Point(348,174,3), new Point(345,174,3), new Point(343,176,3), new Point(340,177,3), new Point(338,178,3), new Point(336,179,3), new Point(334,181,3), new Point(331,182,3), new Point(330,185,3), new Point(328,186,3), new Point(326,187,3), new Point(325,188,3), new Point(324,189,3), new Point(322,190,3), new Point(321,191,3), new Point(321,192,3), new Point(319,193,3), new Point(318,195,3), new Point(317,197,3), new Point(315,198,3), new Point(314,199,3), new Point(314,200,3), new Point(313,200,3)
        ))));

        Gesture required = Gesture.REQUIRED;
        templates.add(new Template(required, new ArrayList<>(Arrays.asList(
            new Point(187,222,1), new Point(186,222,1), new Point(191,222,1), new Point(208,222,1), new Point(239,222,1), new Point(279,222,1), new Point(300,222,1), new Point(325,222,1), new Point(336,222,1), new Point(350,222,1), new Point(354,222,1), new Point(357,222,1), new Point(359,222,1), new Point(362,222,1), new Point(365,222,1), new Point(369,222,1), new Point(372,222,1), new Point(374,222,1), new Point(434,172,2), new Point(432,172,2), new Point(429,172,2), new Point(426,172,2), new Point(420,173,2), new Point(414,176,2), new Point(409,179,2), new Point(405,182,2), new Point(402,186,2), new Point(399,189,2), new Point(397,193,2), new Point(394,197,2), new Point(391,200,2), new Point(389,203,2), new Point(387,205,2), new Point(385,208,2), new Point(383,211,2), new Point(382,214,2), new Point(382,218,2), new Point(382,221,2), new Point(382,225,2), new Point(382,230,2), new Point(382,236,2), new Point(382,241,2), new Point(382,245,2), new Point(384,250,2), new Point(387,255,2), new Point(390,259,2), new Point(396,263,2), new Point(403,267,2), new Point(409,272,2), new Point(416,275,2), new Point(422,276,2), new Point(427,277,2), new Point(432,277,2), new Point(436,277,2), new Point(440,277,2), new Point(443,277,2), new Point(445,277,2), new Point(446,277,2), new Point(447,277,2)            
        ))));

        Gesture realization = Gesture.REALIZATION;
        templates.add(new Template(realization, new ArrayList<>(Arrays.asList(
            new Point(177,143,1), new Point(176,143,1), new Point(171,143,1), new Point(165,143,1), new Point(156,143,1), new Point(146,143,1), new Point(138,143,1), new Point(129,146,1), new Point(121,152,1), new Point(115,161,1), new Point(110,170,1), new Point(109,183,1), new Point(108,194,1), new Point(108,206,1), new Point(110,215,1), new Point(115,223,1), new Point(124,231,1), new Point(134,238,1), new Point(146,242,1), new Point(159,243,1), new Point(173,244,1), new Point(184,242,1), new Point(194,234,1), new Point(202,224,1), new Point(206,213,1), new Point(208,199,1), new Point(209,185,1), new Point(209,175,1), new Point(205,165,1), new Point(199,159,1), new Point(193,154,1), new Point(189,153,1), new Point(181,149,1), new Point(174,149,1), new Point(168,149,1), new Point(161,149,1), new Point(211,184,2), new Point(212,184,2), new Point(221,184,2), new Point(244,184,2), new Point(281,184,2), new Point(325,184,2), new Point(376,184,2), new Point(428,184,2), new Point(447,184,2), new Point(465,184,2), new Point(470,184,2), new Point(474,184,2)
        ))));
        templates.add(new Template(realization, new ArrayList<>(Arrays.asList(
            new Point(164,160,1), new Point(163,160,1), new Point(160,160,1), new Point(156,161,1), new Point(153,164,1), new Point(150,172,1), new Point(148,186,1), new Point(148,200,1), new Point(150,213,1), new Point(157,221,1), new Point(168,229,1), new Point(186,230,1), new Point(202,230,1), new Point(216,223,1), new Point(225,213,1), new Point(233,195,1), new Point(233,184,1), new Point(233,172,1), new Point(225,164,1), new Point(211,161,1), new Point(195,161,1), new Point(183,164,1), new Point(175,168,1), new Point(169,172,1), new Point(240,190,2), new Point(241,190,2), new Point(256,190,2), new Point(283,190,2), new Point(326,190,2), new Point(406,190,2), new Point(446,190,2), new Point(479,190,2), new Point(487,190,2)
        ))));

        Gesture realizationCircle = Gesture.REALIZATION_CIRCLE;
        templates.add(new Template(realizationCircle, new ArrayList<>(Arrays.asList(
            new Point(373,112,1), new Point(372,112,1), new Point(366,112,1), new Point(359,112,1), new Point(352,112,1), new Point(348,112,1), new Point(343,112,1), new Point(339,112,1), new Point(336,113,1), new Point(335,114,1), new Point(333,116,1), new Point(331,120,1), new Point(328,126,1), new Point(326,131,1), new Point(324,136,1), new Point(323,140,1), new Point(321,146,1), new Point(320,150,1), new Point(319,154,1), new Point(319,158,1), new Point(319,163,1), new Point(319,167,1), new Point(319,172,1), new Point(319,177,1), new Point(320,181,1), new Point(322,184,1), new Point(323,186,1), new Point(325,188,1), new Point(327,191,1), new Point(329,193,1), new Point(333,196,1), new Point(336,198,1), new Point(342,200,1), new Point(346,202,1), new Point(351,204,1), new Point(356,204,1), new Point(362,205,1), new Point(369,206,1), new Point(374,206,1), new Point(379,206,1), new Point(383,206,1), new Point(389,206,1), new Point(393,205,1), new Point(398,202,1), new Point(401,199,1), new Point(403,197,1), new Point(407,192,1), new Point(409,188,1), new Point(411,184,1), new Point(412,179,1), new Point(413,173,1), new Point(414,168,1), new Point(414,164,1), new Point(414,159,1), new Point(414,155,1), new Point(414,149,1), new Point(414,144,1), new Point(414,139,1), new Point(414,134,1), new Point(413,130,1), new Point(412,126,1), new Point(410,124,1), new Point(408,120,1), new Point(406,118,1), new Point(404,116,1), new Point(401,113,1), new Point(399,112,1), new Point(397,111,1), new Point(395,110,1), new Point(394,110,1), new Point(391,109,1), new Point(389,109,1), new Point(387,109,1), new Point(384,109,1), new Point(380,109,1), new Point(375,109,1), new Point(371,109,1), new Point(368,109,1), new Point(366,109,1), new Point(362,109,1), new Point(357,109,1), new Point(350,110,1)
        ))));

        Gesture aggregation = Gesture.AGGREGATION;
        templates.add(new Template(aggregation, new ArrayList<>(Arrays.asList(
            new Point(108,129,1), new Point(107,129,1), new Point(105,130,1), new Point(99,135,1), new Point(94,141,1), new Point(84,151,1), new Point(77,158,1), new Point(70,167,1), new Point(67,173,1), new Point(63,177,1), new Point(62,179,1), new Point(62,180,1), new Point(61,181,1), new Point(61,181,1), new Point(62,181,1), new Point(65,184,1), new Point(71,189,1), new Point(78,195,1), new Point(86,203,1), new Point(95,212,1), new Point(104,218,1), new Point(109,222,1), new Point(115,225,1), new Point(119,227,1), new Point(121,228,1), new Point(123,229,1), new Point(124,229,1), new Point(125,229,1), new Point(126,229,1), new Point(127,229,1), new Point(128,229,1), new Point(129,230,1), new Point(131,230,1), new Point(133,230,1), new Point(137,225,1), new Point(143,215,1), new Point(150,204,1), new Point(157,194,1), new Point(165,185,1), new Point(169,180,1), new Point(173,175,1), new Point(174,174,1), new Point(175,173,1), new Point(173,172,1), new Point(170,170,1), new Point(166,166,1), new Point(160,161,1), new Point(156,156,1), new Point(150,150,1), new Point(145,146,1), new Point(141,143,1), new Point(134,139,1), new Point(130,137,1), new Point(127,135,1), new Point(125,134,1), new Point(123,132,1), new Point(121,131,1), new Point(120,130,1), new Point(119,130,1), new Point(118,129,1), new Point(117,129,1), new Point(116,129,1), new Point(115,129,1), new Point(114,129,1), new Point(113,129,1), new Point(112,129,1), new Point(112,128,1), new Point(111,128,1), new Point(111,127,1), new Point(111,127,1), new Point(174,169,2), new Point(175,169,2), new Point(180,169,2), new Point(184,169,2), new Point(190,169,2), new Point(196,169,2), new Point(203,169,2), new Point(208,169,2), new Point(214,169,2), new Point(221,169,2), new Point(227,169,2), new Point(233,169,2), new Point(238,169,2), new Point(243,168,2), new Point(249,168,2), new Point(254,167,2), new Point(259,167,2), new Point(264,166,2), new Point(272,166,2), new Point(277,165,2), new Point(285,165,2), new Point(291,165,2), new Point(299,165,2), new Point(304,164,2), new Point(311,164,2), new Point(317,164,2), new Point(323,163,2), new Point(328,163,2), new Point(333,163,2), new Point(338,163,2), new Point(341,163,2), new Point(345,163,2), new Point(350,163,2), new Point(353,163,2), new Point(357,163,2), new Point(360,163,2), new Point(363,163,2), new Point(366,163,2), new Point(369,162,2), new Point(373,162,2), new Point(376,162,2), new Point(383,161,2), new Point(388,160,2), new Point(395,160,2), new Point(399,159,2), new Point(406,159,2), new Point(411,158,2), new Point(416,157,2), new Point(420,157,2), new Point(424,157,2), new Point(426,157,2), new Point(427,156,2), new Point(428,156,2), new Point(429,156,2), new Point(429,156,2)
        ))));

        Gesture nonnavigable = Gesture.NON_NAVIGABLE;
        templates.add(new Template(nonnavigable, new ArrayList<>(Arrays.asList(
            new Point(397,118,1), new Point(396,118,1), new Point(392,122,1), new Point(385,128,1), new Point(368,145,1), new Point(350,165,1), new Point(337,181,1), new Point(326,195,1), new Point(314,207,1), new Point(310,213,1), new Point(306,216,1), new Point(305,217,1), new Point(304,218,1), new Point(304,219,1), new Point(299,125,2), new Point(302,126,2), new Point(307,130,2), new Point(325,143,2), new Point(345,160,2), new Point(360,173,2), new Point(372,181,2), new Point(379,187,2), new Point(387,193,2), new Point(390,197,2), new Point(393,199,2), new Point(395,200,2), new Point(397,202,2), new Point(398,204,2), new Point(401,205,2), new Point(403,207,2), new Point(404,207,2), new Point(404,208,2), new Point(405,208,2)
        ))));

        Gesture composition = Gesture.COMPOSITION;
        templates.add(new Template(composition, new ArrayList<>(Arrays.asList(
            new Point(155,131,1), new Point(154,131,1), new Point(149,133,1), new Point(145,136,1), new Point(138,142,1), new Point(130,149,1), new Point(121,156,1), new Point(113,161,1), new Point(107,166,1), new Point(104,168,1), new Point(100,172,1), new Point(98,173,1), new Point(97,174,1), new Point(96,175,1), new Point(95,175,1), new Point(96,175,1), new Point(99,176,1), new Point(103,178,1), new Point(108,181,1), new Point(112,183,1), new Point(117,186,1), new Point(123,188,1), new Point(127,190,1), new Point(132,193,1), new Point(137,195,1), new Point(143,197,1), new Point(148,200,1), new Point(154,202,1), new Point(157,202,1), new Point(159,203,1), new Point(160,203,1), new Point(161,203,1), new Point(162,203,1), new Point(162,204,1), new Point(163,204,1), new Point(164,204,1), new Point(164,204,1), new Point(165,203,1), new Point(167,199,1), new Point(172,194,1), new Point(178,189,1), new Point(184,183,1), new Point(191,178,1), new Point(197,173,1), new Point(202,169,1), new Point(206,167,1), new Point(209,165,1), new Point(211,164,1), new Point(212,163,1), new Point(213,163,1), new Point(214,162,1), new Point(215,162,1), new Point(215,160,1), new Point(216,160,1), new Point(217,159,1), new Point(218,159,1), new Point(218,158,1), new Point(217,158,1), new Point(216,158,1), new Point(215,157,1), new Point(211,156,1), new Point(209,156,1), new Point(206,155,1), new Point(202,154,1), new Point(196,152,1), new Point(190,151,1), new Point(183,148,1), new Point(175,144,1), new Point(169,140,1), new Point(164,137,1), new Point(161,134,1), new Point(158,132,1), new Point(156,131,1), new Point(155,130,1), new Point(154,130,1), new Point(154,129,1), new Point(110,174,2), new Point(110,173,2), new Point(110,169,2), new Point(110,164,2), new Point(112,161,2), new Point(117,158,2), new Point(120,155,2), new Point(122,153,2), new Point(125,150,2), new Point(128,148,2), new Point(131,146,2), new Point(134,143,2), new Point(137,140,2), new Point(139,138,2), new Point(141,137,2), new Point(142,136,2), new Point(143,135,2), new Point(144,135,2), new Point(145,135,2), new Point(145,134,2), new Point(147,134,2), new Point(149,133,2), new Point(150,132,2), new Point(151,132,2), new Point(153,132,2), new Point(153,133,2), new Point(153,135,2), new Point(150,137,2), new Point(149,140,2), new Point(147,142,2), new Point(145,144,2), new Point(142,147,2), new Point(139,149,2), new Point(135,152,2), new Point(134,154,2), new Point(133,154,2), new Point(131,154,2), new Point(129,156,2), new Point(126,157,2), new Point(125,158,2), new Point(123,160,2), new Point(122,161,2), new Point(121,162,2), new Point(120,163,2), new Point(119,164,2), new Point(118,166,2), new Point(116,167,2), new Point(114,169,2), new Point(111,170,2), new Point(109,171,2), new Point(108,172,2), new Point(107,173,2), new Point(109,171,2), new Point(114,169,2), new Point(120,164,2), new Point(127,159,2), new Point(133,154,2), new Point(136,152,2), new Point(141,149,2), new Point(144,147,2), new Point(147,145,2), new Point(149,143,2), new Point(151,141,2), new Point(154,139,2), new Point(156,138,2), new Point(157,137,2), new Point(158,137,2), new Point(159,136,2), new Point(161,136,2), new Point(161,137,2), new Point(161,138,2), new Point(159,140,2), new Point(157,143,2), new Point(154,146,2), new Point(150,150,2), new Point(145,156,2), new Point(142,159,2), new Point(138,161,2), new Point(134,163,2), new Point(132,165,2), new Point(129,166,2), new Point(127,168,2), new Point(126,169,2), new Point(124,170,2), new Point(123,170,2), new Point(123,171,2), new Point(122,171,2), new Point(121,171,2), new Point(121,172,2), new Point(121,173,2), new Point(120,173,2), new Point(120,174,2), new Point(119,174,2), new Point(119,175,2), new Point(118,175,2), new Point(118,176,2), new Point(118,175,2), new Point(122,171,2), new Point(128,167,2), new Point(132,164,2), new Point(137,160,2), new Point(141,158,2), new Point(144,156,2), new Point(148,153,2), new Point(152,152,2), new Point(154,151,2), new Point(157,149,2), new Point(159,149,2), new Point(161,149,2), new Point(161,148,2), new Point(162,148,2), new Point(161,148,2), new Point(158,150,2), new Point(153,153,2), new Point(148,157,2), new Point(144,159,2), new Point(139,162,2), new Point(136,164,2), new Point(133,166,2), new Point(131,167,2), new Point(130,168,2), new Point(129,170,2), new Point(128,171,2), new Point(127,172,2), new Point(127,173,2), new Point(126,174,2), new Point(125,174,2), new Point(125,175,2), new Point(124,176,2), new Point(124,177,2), new Point(123,178,2), new Point(125,177,2), new Point(130,172,2), new Point(134,168,2), new Point(139,165,2), new Point(143,161,2), new Point(146,159,2), new Point(149,157,2), new Point(153,155,2), new Point(155,154,2), new Point(157,152,2), new Point(158,151,2), new Point(160,149,2), new Point(162,148,2), new Point(163,147,2), new Point(164,147,2), new Point(165,146,2), new Point(165,146,2), new Point(166,146,2), new Point(167,146,2), new Point(165,148,2), new Point(159,155,2), new Point(153,161,2), new Point(147,166,2), new Point(141,173,2), new Point(137,176,2), new Point(134,178,2), new Point(132,179,2), new Point(131,180,2), new Point(130,180,2), new Point(129,181,2), new Point(128,182,2), new Point(129,182,2), new Point(133,179,2), new Point(140,173,2), new Point(147,168,2), new Point(153,164,2), new Point(159,160,2), new Point(164,158,2), new Point(168,154,2), new Point(171,152,2), new Point(172,151,2), new Point(174,151,2), new Point(175,150,2), new Point(176,149,2), new Point(177,149,2), new Point(177,148,2), new Point(178,148,2), new Point(178,148,2), new Point(178,149,2), new Point(176,151,2), new Point(172,154,2), new Point(169,158,2), new Point(165,161,2), new Point(162,164,2), new Point(158,166,2), new Point(156,168,2), new Point(154,169,2), new Point(152,172,2), new Point(150,174,2), new Point(148,176,2), new Point(147,177,2), new Point(147,178,2), new Point(146,178,2), new Point(146,179,2), new Point(145,180,2), new Point(144,181,2), new Point(143,183,2), new Point(141,184,2), new Point(140,185,2), new Point(140,186,2), new Point(139,187,2), new Point(139,188,2), new Point(140,187,2), new Point(147,182,2), new Point(155,175,2), new Point(164,169,2), new Point(169,164,2), new Point(176,160,2), new Point(179,158,2), new Point(181,155,2), new Point(182,155,2), new Point(183,155,2), new Point(183,154,2), new Point(183,154,2), new Point(183,153,2), new Point(185,152,2), new Point(186,151,2), new Point(187,151,2), new Point(188,150,2), new Point(188,151,2), new Point(186,153,2), new Point(182,155,2), new Point(179,160,2), new Point(176,162,2), new Point(174,165,2), new Point(172,167,2), new Point(170,169,2), new Point(166,171,2), new Point(164,173,2), new Point(162,176,2), new Point(159,179,2), new Point(157,181,2), new Point(156,182,2), new Point(156,183,2), new Point(156,184,2), new Point(155,184,2), new Point(154,185,2), new Point(153,186,2), new Point(153,187,2), new Point(152,187,2), new Point(152,188,2), new Point(152,189,2), new Point(152,190,2), new Point(152,192,2), new Point(151,193,2), new Point(151,194,2), new Point(152,193,2), new Point(157,188,2), new Point(163,182,2), new Point(169,176,2), new Point(172,173,2), new Point(175,170,2), new Point(178,168,2), new Point(180,166,2), new Point(182,164,2), new Point(185,163,2), new Point(187,161,2), new Point(190,159,2), new Point(192,158,2), new Point(194,157,2), new Point(196,156,2), new Point(196,155,2), new Point(197,155,2), new Point(197,156,2), new Point(195,159,2), new Point(192,166,2), new Point(188,170,2), new Point(185,174,2), new Point(181,177,2), new Point(178,180,2), new Point(175,183,2), new Point(173,186,2), new Point(171,188,2), new Point(169,189,2), new Point(168,190,2), new Point(167,192,2), new Point(166,192,2), new Point(165,193,2), new Point(164,194,2), new Point(163,194,2), new Point(164,193,2), new Point(167,190,2), new Point(169,188,2), new Point(172,184,2), new Point(175,182,2), new Point(178,180,2), new Point(179,178,2), new Point(183,175,2), new Point(186,173,2), new Point(188,171,2), new Point(192,169,2), new Point(194,167,2), new Point(195,166,2), new Point(196,165,2), new Point(197,165,2), new Point(198,164,2), new Point(199,164,2), new Point(199,163,2), new Point(200,163,2), new Point(201,162,2), new Point(202,161,2), new Point(203,160,2), new Point(203,160,2), new Point(203,159,2), new Point(205,159,2), new Point(205,158,2), new Point(206,158,2), new Point(207,158,2), new Point(207,158,2), new Point(206,157,2), new Point(203,156,2), new Point(201,154,2), new Point(199,152,2), new Point(197,152,2), new Point(196,151,2), new Point(195,151,2), new Point(195,150,2), new Point(194,150,2), new Point(193,150,2), new Point(193,149,2), new Point(192,149,2), new Point(191,149,2), new Point(190,149,2), new Point(189,148,2), new Point(188,148,2), new Point(187,148,2), new Point(186,147,2), new Point(185,147,2), new Point(185,146,2), new Point(184,146,2), new Point(183,145,2), new Point(183,145,2), new Point(183,144,2), new Point(182,144,2), new Point(182,143,2), new Point(181,143,2), new Point(181,142,2), new Point(180,142,2), new Point(180,141,2), new Point(179,141,2), new Point(178,141,2), new Point(177,141,2), new Point(176,141,2), new Point(175,139,2), new Point(175,138,2), new Point(174,138,2), new Point(173,137,2), new Point(172,137,2), new Point(171,136,2), new Point(170,136,2), new Point(170,136,2), new Point(170,135,2), new Point(169,135,2), new Point(168,134,2), new Point(167,134,2), new Point(166,134,2), new Point(166,133,2), new Point(165,133,2), new Point(164,133,2), new Point(163,132,2), new Point(162,132,2), new Point(161,131,2), new Point(160,131,2), new Point(160,130,2), new Point(159,130,2), new Point(159,130,2), new Point(158,130,2), new Point(157,130,2), new Point(156,130,2), new Point(218,157,3), new Point(219,157,3), new Point(221,157,3), new Point(226,157,3), new Point(228,157,3), new Point(232,157,3), new Point(235,157,3), new Point(239,157,3), new Point(243,157,3), new Point(249,157,3), new Point(254,157,3), new Point(260,157,3), new Point(264,157,3), new Point(268,157,3), new Point(272,157,3), new Point(275,157,3), new Point(279,157,3), new Point(283,157,3), new Point(287,157,3), new Point(291,157,3), new Point(295,157,3), new Point(299,157,3), new Point(302,157,3), new Point(307,157,3), new Point(310,157,3), new Point(313,157,3), new Point(316,157,3), new Point(319,157,3), new Point(321,157,3), new Point(323,157,3), new Point(326,157,3), new Point(328,157,3), new Point(331,157,3), new Point(335,157,3), new Point(338,157,3), new Point(340,157,3), new Point(343,157,3), new Point(346,157,3), new Point(348,157,3), new Point(351,157,3), new Point(354,157,3), new Point(356,157,3), new Point(359,157,3), new Point(361,157,3), new Point(364,157,3), new Point(366,157,3), new Point(369,157,3), new Point(372,156,3), new Point(374,156,3), new Point(378,156,3), new Point(381,156,3), new Point(384,156,3), new Point(389,156,3), new Point(391,155,3), new Point(394,155,3), new Point(396,155,3), new Point(398,154,3), new Point(399,154,3), new Point(402,154,3), new Point(403,154,3), new Point(404,154,3), new Point(405,153,3), new Point(406,153,3), new Point(407,153,3), new Point(409,153,3), new Point(410,153,3), new Point(412,153,3), new Point(413,152,3), new Point(415,152,3), new Point(417,152,3), new Point(419,152,3), new Point(421,152,3), new Point(424,152,3), new Point(426,152,3), new Point(428,152,3), new Point(429,152,3), new Point(432,152,3), new Point(434,152,3), new Point(435,152,3), new Point(437,152,3), new Point(439,153,3), new Point(440,153,3), new Point(442,153,3), new Point(443,153,3), new Point(444,153,3)
        ))));

        Gesture dependency = Gesture.DEPENDENCY;
        templates.add(new Template(dependency, new ArrayList<>(Arrays.asList(
            new Point(108,215,1), new Point(109,215,1), new Point(112,215,1), new Point(116,215,1), new Point(117,215,1), new Point(118,215,1), new Point(119,215,1), new Point(125,215,1), new Point(127,215,1), new Point(131,215,1), new Point(133,215,1), new Point(134,215,1), new Point(175,213,2), new Point(176,213,2), new Point(179,213,2), new Point(184,213,2), new Point(189,213,2), new Point(193,213,2), new Point(198,213,2), new Point(200,213,2), new Point(203,213,2), new Point(204,213,2), new Point(205,213,2), new Point(249,212,3), new Point(250,212,3), new Point(253,212,3), new Point(257,212,3), new Point(259,212,3), new Point(262,212,3), new Point(263,212,3), new Point(265,212,3), new Point(267,212,3), new Point(270,212,3), new Point(272,212,3), new Point(274,212,3), new Point(276,212,3), new Point(279,212,3), new Point(282,212,3), new Point(287,212,3), new Point(290,212,3), new Point(293,212,3), new Point(295,212,3), new Point(296,212,3), new Point(297,212,3), new Point(299,212,3), new Point(300,212,3), new Point(301,212,3), new Point(336,211,4), new Point(339,211,4), new Point(345,211,4), new Point(354,211,4), new Point(365,211,4), new Point(371,211,4), new Point(377,211,4), new Point(383,211,4), new Point(387,211,4), new Point(389,211,4), new Point(391,211,4), new Point(394,211,4), new Point(396,211,4), new Point(399,211,4), new Point(400,211,4), new Point(401,211,4), new Point(402,211,4), new Point(402,210,4), new Point(402,209,4), new Point(401,208,4), new Point(398,206,4), new Point(396,204,4), new Point(393,200,4), new Point(391,199,4), new Point(389,197,4), new Point(387,196,4), new Point(386,195,4), new Point(384,195,4), new Point(383,195,4), new Point(382,193,4), new Point(381,193,4), new Point(380,193,4), new Point(380,193,4), new Point(382,193,4), new Point(388,196,4), new Point(395,201,4), new Point(403,205,4), new Point(407,207,4), new Point(411,211,4), new Point(413,212,4), new Point(414,213,4), new Point(415,213,4), new Point(415,214,4), new Point(415,214,4), new Point(412,216,4), new Point(409,219,4), new Point(406,221,4), new Point(404,224,4), new Point(401,226,4), new Point(399,228,4), new Point(397,230,4), new Point(395,232,4), new Point(393,234,4), new Point(392,235,4), new Point(390,236,4), new Point(389,237,4), new Point(389,237,4)
        ))));

        Gesture realizationDependency = Gesture.REALIZATION_DEPENDENCY;
        templates.add(new Template(realizationDependency, new ArrayList<>(Arrays.asList(
            new Point(91,179,1), new Point(92,179,1), new Point(96,179,1), new Point(103,179,1), new Point(111,179,1), new Point(116,179,1), new Point(118,179,1), new Point(119,179,1), new Point(121,179,1), new Point(123,179,1), new Point(124,179,1), new Point(125,179,1), new Point(126,179,1), new Point(176,178,2), new Point(178,178,2), new Point(184,178,2), new Point(193,178,2), new Point(199,178,2), new Point(209,178,2), new Point(212,178,2), new Point(214,178,2), new Point(215,178,2), new Point(273,176,3), new Point(276,176,3), new Point(285,176,3), new Point(290,176,3), new Point(296,176,3), new Point(297,176,3), new Point(298,176,3), new Point(339,175,4), new Point(341,175,4), new Point(344,175,4), new Point(349,175,4), new Point(355,175,4), new Point(357,175,4), new Point(359,175,4), new Point(360,175,4), new Point(361,175,4), new Point(362,175,4), new Point(363,175,4), new Point(365,175,4), new Point(367,175,4), new Point(370,175,4), new Point(372,174,4), new Point(373,174,4), new Point(376,173,4), new Point(378,173,4), new Point(381,172,4), new Point(383,172,4), new Point(385,171,4), new Point(389,171,4), new Point(391,171,4), new Point(394,171,4), new Point(396,171,4), new Point(397,171,4), new Point(398,171,4), new Point(398,170,4), new Point(397,167,4), new Point(395,163,4), new Point(393,158,4), new Point(392,152,4), new Point(391,147,4), new Point(391,142,4), new Point(391,139,4), new Point(391,136,4), new Point(391,134,4), new Point(391,134,4), new Point(391,133,4), new Point(391,131,4), new Point(391,130,4), new Point(391,128,4), new Point(391,127,4), new Point(391,126,4), new Point(392,126,4), new Point(395,130,4), new Point(400,136,4), new Point(405,142,4), new Point(411,149,4), new Point(417,154,4), new Point(423,160,4), new Point(429,165,4), new Point(432,167,4), new Point(436,170,4), new Point(439,171,4), new Point(442,173,4), new Point(443,174,4), new Point(444,175,4), new Point(444,175,4), new Point(444,176,4), new Point(444,178,4), new Point(442,180,4), new Point(439,184,4), new Point(437,187,4), new Point(433,191,4), new Point(430,195,4), new Point(425,199,4), new Point(421,203,4), new Point(417,206,4), new Point(414,208,4), new Point(410,211,4), new Point(408,213,4), new Point(406,215,4), new Point(405,216,4), new Point(404,217,4), new Point(404,217,4), new Point(404,217,4), new Point(403,215,4), new Point(403,211,4), new Point(402,208,4), new Point(401,205,4), new Point(401,201,4), new Point(400,198,4), new Point(399,194,4), new Point(399,191,4), new Point(398,188,4), new Point(398,187,4), new Point(398,186,4), new Point(397,185,4), new Point(397,184,4), new Point(397,183,4), new Point(397,182,4), new Point(397,181,4), new Point(397,179,4), new Point(397,176,4), new Point(397,174,4), new Point(397,172,4), new Point(397,170,4), new Point(396,168,4), new Point(396,166,4), new Point(395,164,4), new Point(394,162,4), new Point(394,159,4), new Point(393,157,4)
        ))));
    }

    public static ArrayList<RecognizerResult> recognize(ArrayList<Point> points) {
        if (points.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Point> normalizedPoints = normalize(points);
        double score = Double.POSITIVE_INFINITY;
        HashMap<Gesture, Double> result = new HashMap<>();

        for (Template template : templates) {
            double d = greedyCloudMatch(normalizedPoints, template.points);
            result.put(template.gesture, d);
            if (d < score) score = d;
        }

        if (score == Double.POSITIVE_INFINITY) {
            return new ArrayList<>();
        }

        Dimension drawingSize = getSize(points);
        ArrayList<RecognizerResult> convertedResult = new ArrayList<>(result.size());
        for (Map.Entry<Gesture, Double> r : result.entrySet()) {
            if (r.getValue() - score < 0.2) {
                convertedResult.add(new RecognizerResult(r.getKey(), r.getValue(), drawingSize));
            }
        }
        Collections.sort(convertedResult);

        return convertedResult;
    }

    private static Dimension getSize(ArrayList<Point> points) {
        double minX, minY, maxX, maxY;
        minX = minY = Double.POSITIVE_INFINITY;
        maxX = maxY = Double.NEGATIVE_INFINITY;

        for (Point p : points) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        return new Dimension((int) (maxX - minX + 1), (int) (maxY - minY + 1));
    }

    private static double greedyCloudMatch(ArrayList<Point> points, ArrayList<Point> templatePoints) {
        double e = 0.50;
        double step = Math.floor(Math.pow(N, 1 - e));
        double min = Double.POSITIVE_INFINITY;

        for (int i = 0; i < N; i += step) {
            double d1 = cloudDistance(points, templatePoints, i);
            double d2 = cloudDistance(templatePoints, points, i);
            min = Math.min(min, Math.min(d1, d2));
        }

        return min;
    }

    private static double cloudDistance(ArrayList<Point> points, ArrayList<Point> templatePoints, int start) {
        if (points.size() != templatePoints.size()) {
            return Double.POSITIVE_INFINITY;
        }

        boolean[] matched = new boolean[N];
        double sum = 0;
        int i = start;
        do {
            int index = -1;
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < matched.length; j++) {
                if (!matched[j]) {
                    double d = getPointDistance(points.get(i), templatePoints.get(j));
                    if (d < min) {
                        min = d;
                        index = j;
                    }
                }
            }
            matched[index] = true;
            double weight = 1 - ((i - start + N) % N) / (double) N;
            sum += weight * min;
            i = (i + 1) % N;
        } while (i != start);

        return sum;
    }

    private static ArrayList<Point> resample(ArrayList<Point> points) {
        double incrementLength = getTotalPathDistance(points) / (N - 1);

        ArrayList<Point> newPoints = new ArrayList<>(N);
        newPoints.add(points.get(0));

        double currentDistance = 0;

        for (int i = 1; i < points.size(); i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            if (prev.strokeID == curr.strokeID) {
                double d = getPointDistance(prev, curr);
                if ((currentDistance + d) >= incrementLength) {
                    double qx = prev.x + ((incrementLength - currentDistance) / d) * (curr.x - prev.x);
                    double qy = prev.y + ((incrementLength - currentDistance) / d) * (curr.y - prev.y);
                    Point q = new Point(qx, qy, curr.strokeID);
                    newPoints.add(q);
                    points.add(i, q);
                    currentDistance = 0;
                } else {
                    currentDistance += d;
                }
            }
        }

        if (newPoints.size() < N) {
            newPoints.add(points.get(points.size() - 1));
        }

        return newPoints;
    }

    private static ArrayList<Point> scale(ArrayList<Point> points) {
        double minX, minY, maxX, maxY;
        minX = minY = Double.POSITIVE_INFINITY;
        maxX = maxY = 0;

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        double scale = Math.max(maxX - minX, maxY - minY);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x = (point.x - minX) / scale;
            point.y = (point.y - minY) / scale;
        }

        return points;
    }

    private static ArrayList<Point> translate(ArrayList<Point> points) {
        Point c = getCentroid(points);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            p.x -= c.x;
            p.y -= c.y;
        }

        return points;
    }

    private static Point getCentroid(ArrayList<Point> points) {
        Point c = new Point(0, 0, -1);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            c.x += p.x;
            c.y += p.y;
        }

        c.x /= N;
        c.y /= N;

        return c;
    }

    private static ArrayList<Point> rotateToZero(ArrayList<Point> points) {
        Point c = getCentroid(points);
        double t = Math.atan2(c.y - points.get(0).y, c.x - points.get(0).x);
        return rotateBy(points, -t);
    }

    private static ArrayList<Point> rotateBy(ArrayList<Point> points, double t) {
        ArrayList<Point> newPoints = new ArrayList<>(points.size());
        Point c = getCentroid(points);
        for (Point point : points) {
            double qx = (point.x - c.x) * Math.cos(t) - (point.y - c.y) * Math.sin(t) + c.x;
            double qy = (point.x - c.x) * Math.sin(t) + (point.y - c.y) * Math.cos(t) + c.y;
            newPoints.add(new Point(qx, qy, point.strokeID));
        }
        return newPoints;
    }

    protected static ArrayList<Point> normalize(ArrayList<Point> points) {
        points = resample(points);
        points = rotateToZero(points);
        return translate(scale(points));
    }

    private static double getPointDistance(Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    private static double getTotalPathDistance(ArrayList<Point> points) {
        double distance = 0;
        for (int i = 1; i < points.size(); i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            if (prev.strokeID == curr.strokeID) {
                // add the distances between points for the same stroke
                distance += getPointDistance(prev, curr);
            }
        }
        return distance;
    }

}
