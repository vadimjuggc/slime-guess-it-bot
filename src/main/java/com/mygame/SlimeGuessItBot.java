package com.mygame;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class SlimeGuessItBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "@guessIt25_bot";
    }

    private HashMap<Long, Track> currentTrack = new HashMap<>();
    private HashMap<Long, List<Track>> currentOptions = new HashMap<>();
    private TrackDatabase db = new TrackDatabase();
    private HashMap<Long, Integer> score = new HashMap<>();
    private HashMap<Long, List<Track>> usedTracks = new HashMap<>();
    private HashMap<Long, Long> startTime = new HashMap<>();
    private HashMap<Long, List<Double>> wastedTime = new HashMap<>();
    private HashMap<Long, String> playerNames = new HashMap<>();

    @Override
    public String getBotToken() {
        return "8963846462:AAFNt_0SjP87adp4By7zkTyuFMatkrzzzTI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (text.equals("/start")) {
                sendMessage(chatId, "Привет! Я GuessIt бот. Напиши /play чтобы начать игру!");
                String name = update.getMessage().getFrom().getFirstName();
                playerNames.put(chatId, name);
            }
            if (text.equals("/play")) {
                score.remove(chatId);
                startGame(chatId);
            }
            if (text.equals("/stop")) {
                stopGame(chatId);
            }
            if (text.equals("/leaderboard")) {
                showLeaderboard(chatId);
            }
        }

        if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            checkAnswer(chatId, data, update);
        }
    }

    private void startGame(long chatId) {
        List <Track> used = usedTracks.getOrDefault(chatId, new ArrayList<>());
        Track correct = db.getRandomTrack(used);
        if (correct == null) stopGame(chatId);
        used.add(correct);
        usedTracks.put(chatId, used);
        List<Track> wrong = db.getWrongOptions(correct);
        currentTrack.put(chatId, correct);

        List<Track> options = new ArrayList<>(wrong);
        options.add(correct);
        Collections.shuffle(options);
        currentOptions.put(chatId, options);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(options.get(i).getTitle());
            button.setCallbackData(String.valueOf(i));
            rows.add(List.of(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(String.valueOf(chatId));
        sendVoice.setVoice(new InputFile(new File(correct.getAudioPath())));
        sendVoice.setReplyMarkup(markup);
        startTime.put(chatId, System.currentTimeMillis());

        try {
            execute(sendVoice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void stopGame(long chatId) {
        if (!currentTrack.containsKey(chatId))
        {
            sendMessage(chatId, "Игра еще не запущена. /start для начала.");
            return;
        }
        int finalScore = score.getOrDefault(chatId, 0);
        double finalTime = wastedTime.getOrDefault(chatId, new ArrayList<>()).stream().mapToDouble(Double::doubleValue).sum();
        String time = String.format("%.2f", finalTime);
        sendMessage(chatId, "Игра окончена." + " Время: " + time + " c.");
        score.remove(chatId);
        currentTrack.remove(chatId);
        currentOptions.remove(chatId);
    }

    private void showLeaderboard(long chatId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Лидерборд:\n\n");
        score.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> {
                    String name = playerNames.getOrDefault(entry.getKey(), "Unknown");
                    String marker = entry.getKey() == chatId ? " ◀" : "";
                    sb.append(name + ": " + entry.getValue() + marker + "\n");
                });

        sendMessage(chatId, sb.toString());
    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void checkAnswer(long chatId, String text, Update update) {
        List<Track> options = currentOptions.get(chatId);
        List<Double> times = wastedTime.getOrDefault(chatId, new ArrayList<>());
        Track correct = currentTrack.get(chatId);
        int index = Integer.parseInt(text);
        double seconds = (System.currentTimeMillis() - startTime.get(chatId)) / 1000.0;
        String time = String.format("%.2f", seconds);
        if (options.get(index).equals(correct)) {
            score.put(chatId, score.getOrDefault(chatId, 0) + 1);
            sendMessage(chatId, "Правильно! Счёт: " + score.get(chatId));
            times.add(seconds);
            startGame(chatId);
        } else {
            score.put(chatId, 0);
            times.add(seconds);
            sendMessage(chatId, "Неправильно! Это был трек: " + correct.getTitle());
            startGame(chatId);
        }
        wastedTime.put(chatId, times);
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(update.getCallbackQuery().getId());
        answer.setText("Правильно! Счёт: " + score.get(chatId) + ". " + time + " c.");
        answer.setShowAlert(false);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
