package com.newtube.mobile.ui.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.app.Application;

import com.liskovsoft.smartyoutubetv2.common.app.models.data.Video;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.ConscryptMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** NEWTUBE(shorts): the phone's Show Shorts setting controls list and channel-tab filtering. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, manifest = Config.NONE, application = Application.class)
@ConscryptMode(ConscryptMode.Mode.OFF)
public class ShortsFilterTest {
    @Test
    public void everyShortIsDroppedAndTheRestKeepsItsOrder() {
        Video a = video("a", false);
        Video s1 = video("s1", true);
        Video b = video("b", false);
        Video s2 = video("s2", true);

        assertEquals(Arrays.asList(a, b), ShortsFilter.withoutShorts(Arrays.asList(s1, a, s2, b), false));
    }

    @Test
    public void aListWithoutShortsIsReturnedAsIs() {
        List<Video> feed = Arrays.asList(video("a", false), video("b", false));
        assertSame(feed, ShortsFilter.withoutShorts(feed, false));
        assertNull(ShortsFilter.withoutShorts(null, false));
    }

    @Test
    public void aShelfOfShortsLeavesNothing() {
        assertEquals(new ArrayList<Video>(),
                ShortsFilter.withoutShorts(Arrays.asList(video("s1", true), video("s2", true)), false));
    }

    @Test
    public void nullSlotsAreNotShorts() {
        Video a = video("a", false);
        assertEquals(Arrays.asList(null, a),
                ShortsFilter.withoutShorts(Arrays.asList(null, video("s", true), a), false));
        assertFalse(ShortsFilter.isShort(null));
    }

    @Test
    public void theChannelsShortsSectionIsNamedShortsAndHoldsOnlyShorts() {
        List<Video> shorts = Arrays.asList(video("s1", true), video("s2", true));
        assertTrue(ShortsFilter.isShortsSection("Shorts", "Shorts", shorts, false));
        assertTrue(ShortsFilter.isShortsSection(" SHORTS ", null, shorts, false));
        assertTrue("localized label", ShortsFilter.isShortsSection("Cortos", "Cortos", shorts, false));
        assertTrue("already emptied by the service", ShortsFilter.isShortsSection("Shorts", null,
            new ArrayList<>(), false));
    }

    @Test
    public void aNormalSectionWhoseFirstPageIsAllShortsIsNotTheShortsSection() {
        List<Video> shorts = Arrays.asList(video("s1", true), video("s2", true));
        assertFalse(ShortsFilter.isShortsSection("Videos", "Shorts", shorts, false));
        assertFalse(ShortsFilter.isShortsSection(null, "Shorts", shorts, false));
        assertFalse("a Shorts-named row with real videos is kept",
            ShortsFilter.isShortsSection("Shorts", "Shorts",
                Arrays.asList(video("s1", true), video("a", false)), false));
        }

        @Test
        public void showingShortsLeavesListsAndChannelSectionsIntact() {
        List<Video> feed = Arrays.asList(video("s", true), video("a", false));

        assertSame(feed, ShortsFilter.withoutShorts(feed, true));
        assertFalse(ShortsFilter.isShortsSection("Shorts", "Shorts", feed, true));
    }

    private static Video video(String id, boolean isShorts) {
        Video video = new Video();
        video.videoId = id;
        video.title = id;
        video.isShorts = isShorts;
        return video;
    }
}
