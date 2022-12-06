//package com.example.cbu.telegramBot;
//
//import com.example.cbu.entity.UserBot;
//import com.example.cbu.telegramBot.enums.BotState;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import javax.annotation.PostConstruct;
//
//@Service
//public class TelegramBotService extends TelegramLongPollingBot {
//
//    @Autowired
//    private CodeUtils codeUtils;
//
//    @Autowired
//    private UserBotRepository userBotRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Value("${telegram.bot.username}")
//    private String botUsername;
//
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
//    @PostConstruct
//    public void registerBot() {
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//        try {
//            botsApi.registerBot(this);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String receivedMessage = update.getMessage().getText();
//            SendMessage sendMessage = null;
//
//            // TODO: futuramente, tratar casos onde um usuário chama um comando sem ainda estar autenticado
//            switch (receivedMessage) {
//                case "/autenticar":
//                    sendMessage = handleAuthentication(update);
//                    break;
//                default:
//                    // Quando nenhum comando atender, será um texto a ser checado de acordo com o estado anterior
//                    sendMessage = checkState(update);
//            }
//
//            try {
//                execute(sendMessage);
//            } catch (TelegramApiException e) {
//                codeUtils.log(e.getMessage(), this);
//            }
//        }
//    }
//
//    private SendMessage handleAuthentication(Update update) {
//        SendMessage sendMessage = new SendMessage()
//                .setChatId(update.getMessage().getChatId())
//                .setText(BotState.AUTH_STEP_1.toString());
//
//        UserBot userBot = userBotRepository.findByChatId(update.getMessage().getChatId().toString());
//
//        if (userBot == null) {
//            userBot = new UserBot();
//            userBot.setChatId(update.getMessage().getChatId().toString());
//            userBot.setLastBotState(BotState.AUTH_STEP_1);
//        } else if (userBot.isVerified()) {
//            // Um texto simples enviado no sendMessage indica o fim de um fluxo
//            sendMessage.setText("Este aparelho já está autenticado no sistema.");
//            userBot.setLastBotState(null);
//        }
//
//        userBotRepository.save(userBot);
//        return sendMessage;
//    }
//
//    // Checa o estado anterior do bot em relação ao chatId recebido
//    private SendMessage checkState(Update update) {
//        UserBot userBot = userBotRepository.findByChatId(update.getMessage().getChatId().toString());
//        SendMessage sendMessage = null;
//
//        if (userBot == null || userBot.getLastBotState() == null)
//            return sendDefaultMessage(update);
//
//        switch (Optional.ofNullable(userBot.getLastBotState()).orElse(BotState.NO_STATE)) {
//            case AUTH_STEP_1:
//                sendMessage = sendCode(update);
//                break;
//            case AUTH_STEP_2:
//                sendMessage = validateCode(update);
//                break;
//            default:
//                sendMessage = sendDefaultMessage(update);
//        }
//
//        return sendMessage;
//    }
//
//    // Grava o código no banco e envia para o e-mail do usuário
//    private SendMessage sendCode(Update update) {
//        User user = userRepository.findByEmail(update.getMessage().getText().toLowerCase());
//        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "");
//
//        if (user == null)
//            sendMessage.setText("Não encontrei nenhum usuário no sistema com este e-mail :(");
//        else {
//            UserBot userBot = userBotRepository.findByChatId(update.getMessage().getChatId().toString());
//
//            String verificationCode = Integer.toString(new Random().nextInt(899999) + 100000);
//            String text = "Este é um e-mail automático de verificação de identidade. Informe este código para o bot do Telegram: " + verificationCode;
//            codeUtils.sendEmail(new String[]{user.getEmail()}, "CCR Laudos - Código de Verificação", text);
//
//            // Associa a conversação ao usuário, mas a validade depende da flag verified
//            userBot.setUser(user);
//            userBot.setBotVerificationCode(verificationCode);
//            userBot.setLastBotState(BotState.AUTH_STEP_2);
//            userBotRepository.save(userBot);
//
//            sendMessage.setText(BotState.AUTH_STEP_2.toString());
//        }
//
//        return sendMessage;
//    }
//
//    // Checa se o código informado foi o mesmo passado por e-mail para o usuário a fim de autenticá-lo
//    private SendMessage validateCode(Update update) {
//        UserBot userBot = userBotRepository.findByChatId(update.getMessage().getChatId().toString());
//        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "");
//
//        if (update.getMessage().getText().equals(userBot.getBotVerificationCode())) {
//            userBot.setVerified(true);
//            sendMessage.setText("O aparelho foi autenticado com sucesso. Você passará a receber notificações do sistema.");
//        } else {
//            userBot.setUser(null);
//            sendMessage.setText("Código inválido.");
//        }
//
//        userBotRepository.save(userBot);
//        return sendMessage;
//    }
//
//    private SendMessage sendDefaultMessage(Update update) {
//        String markdownMessage = "Não entendi \ud83e\udd14 \n"
//                + "Que tal tentar um comando digitando */* ?";
//