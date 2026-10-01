package com.mygame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrackDatabase {
    private List<Track> trackList;

    public TrackDatabase(List<Track> trackList) {
        this.trackList = trackList;
    }

    public TrackDatabase() {
        this.trackList = new ArrayList<>();
        trackList.add(new Track("Ref", "Osamason", "src/tracks/osamason_ref.mp3"));
        trackList.add(new Track("Made Sum Plans", "Osamason", "src/tracks/osamason_made_sum_plans.mp3"));
        trackList.add(new Track("Break Da News", "Osamason", "src/tracks/osamason_break_da_news.mp3"));
        trackList.add(new Track("The Whole World Is Free", "Osamason", "src/tracks/osamason_the_whole_world_is_free.mp3"));

        // Nettspend
        trackList.add(new Track("Nothing like uuu", "Nettspend", "src/tracks/nettspend_nothing_like_uuu.mp3"));
        trackList.add(new Track("lil bieber", "Nettspend", "src/tracks/nettspend_lil_bieber.mp3"));
        trackList.add(new Track("shades on", "Nettspend", "src/tracks/nettspend_shades_on.mp3"));
        trackList.add(new Track("What they say", "Nettspend", "src/tracks/nettspend_what_they_say.mp3"));

        // 1oneam
        trackList.add(new Track("Vogue", "1oneam", "src/tracks/1oneam_vogue.mp3"));
        trackList.add(new Track("Death of Me", "1oneam", "src/tracks/1oneam_death_of_me.mp3"));
        trackList.add(new Track("Bless Up", "1oneam", "src/tracks/1oneam_bless_up.mp3"));
        trackList.add(new Track("Penthouse", "1oneam", "src/tracks/1oneam_penthouse.mp3"));

        // Che
        trackList.add(new Track("Pizza Time", "Che", "src/tracks/che_pizza_time.mp3"));
        trackList.add(new Track("DIOR LEOPARD", "Che", "src/tracks/che_dior_leopard.mp3"));
        trackList.add(new Track("I Rot, I Rot.", "Che", "src/tracks/che_i_rot_i_rot.mp3"));
        trackList.add(new Track("agenda", "Che", "src/tracks/che_agenda.mp3"));

        // bleood
        trackList.add(new Track("i ˂3 seals", "bleood", "src/tracks/bleood_i_3_seals.mp3"));
        trackList.add(new Track("gygjfacb", "bleood", "src/tracks/bleood_gygjfacb.mp3"));
        trackList.add(new Track("on E", "bleood", "src/tracks/bleood_on_e.mp3"));
        trackList.add(new Track("custo a burger", "bleood", "src/tracks/bleood_custo_a_burger.mp3"));
    }

    public Track getRandomTrack(List<Track> used) {
        List<Track> withAudio = new ArrayList<>();
        for (Track track : trackList) {
            if (track.getAudioPath() != null && !used.contains(track))
                withAudio.add(track);
        }
        if (withAudio.isEmpty()) return null;
        return withAudio.get(new Random().nextInt(withAudio.size()));
    }

    public List<Track> getWrongOptions(Track correct) {
        List<Track> wrong = new ArrayList<>(trackList);
        wrong.remove(correct);
        Collections.shuffle(wrong);
        return wrong.subList(0, 3);
    }
}
