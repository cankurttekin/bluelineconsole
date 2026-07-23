package net.cankurttekin.andmenu.commandSearchers.eachSearcher;

import android.content.Context;

import androidx.annotation.NonNull;

import net.cankurttekin.andmenu.commands.help.HelpCandidateEntry;
import net.cankurttekin.andmenu.interfaces.CandidateEntry;
import net.cankurttekin.andmenu.interfaces.CommandSearcher;

import java.util.ArrayList;
import java.util.List;

public class HelpCommandSearcher implements CommandSearcher {
    @Override
    public void refresh(Context context) {

    }

    @Override
    public void close() {}

    @Override
    public boolean isPrepared() {
        return true;
    }

    @Override
    public void waitUntilPrepared() {}

    @Override
    @NonNull
    public List<CandidateEntry> searchCandidateEntries(String query, Context context) {
        List<CandidateEntry> candidates = new ArrayList<>();

        if (query.equalsIgnoreCase("help")) {
            candidates.add(new HelpCandidateEntry());
        }

        return candidates;
    }
}
