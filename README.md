# AndroidAutoSDL

**Android Auto projection over SmartDeviceLink for the Toyota PLAY+.**

[English](README.md) · [Português brasileiro](README.pt-BR.md)

<img src="docs/images/open-headunit-icon.png" width="96" height="96" alt="Open Headunit icon" />

AndroidAutoSDL is an experimental extension of [Open Headunit](https://github.com/andreknieriem/open-headunit). It runs on an Android phone and bridges the Android Auto interface to a SmartDeviceLink (SDL) head unit over USB, bringing navigation and touch interaction to existing hardware without native Android Auto.

Development and physical testing focus on the **Toyota PLAY+ in a 2019 Toyota Yaris**. Other vehicles and firmware versions have not been validated.

## Origin and purpose

[Open Headunit, maintained by andreknieriem and its contributors](https://github.com/andreknieriem/open-headunit), provides the Android Auto receiver, Self Mode, audio handling and much of the application UI. This project builds on that work; it does not implement Android Auto from scratch.

Our starting snapshot is upstream commit [`667346188bee1c50f300e245f373d13942fbd220`](https://github.com/andreknieriem/open-headunit/tree/667346188bee1c50f300e245f373d13942fbd220). AndroidAutoSDL adds SDL transport and a dedicated projection profile. Its goals are reliable navigation and touch on the tested central, separate settings for this mode, and a reproducible record of builds and tests. The original TomTom APK was inspected as a compatibility reference and was not modified.

## What this project adds

- USB SDL registration, accessory permissions, display capability discovery and diagnostics using SDL Android SDK 4.12.2.
- An EGL/SurfaceTexture relay between Android Auto decoding and SDL H.264 encoding, with scaling and fixes for local-display watchdog interruptions.
- SDL touch events mapped into Android Auto coordinates.
- A dedicated SDL page and phone streaming screen, panel geometry, saved DPI and physical aspect correction, including a 115% preset.
- Start, stop, disconnect and optional automatic video start when authorized by the central.
- Independent microphone selection: phone, automatic or Bluetooth, with the effective input shown during capture.
- An optional Open Headunit icon upload after SDL registration, added in 0.14 without changing the compatibility identity.
- Driving notifications forwarded as Android Auto restrictions; head-unit HMI and video-streaming refusals remain respected.

```text
Android Auto → Open Headunit SDL profile → frame relay / H.264 → USB SDL → PLAY+
Android Auto ← touch-coordinate mapping ← SDL touch events ←─────────── PLAY+
Media audio ──────────────────────────── Bluetooth ───────────────────→ speakers
Selected microphone ──────────────────→ Android Auto voice input
```

Media audio currently uses **Bluetooth**, not SDL USB audio. With media-only Bluetooth, select **Celular / Phone** for voice input. Android may override a preferred microphone route; the SDL page reports the effective input. Telephone-call routing remains controlled by the phone and Bluetooth system.

## Latest build and test status

**Bridge 0.14 — experimental icon build**

[APK](artifacts/AndroidAutoSDL-Bridge/v0.14/AndroidAuto-SDL-Bridge-0.14.apk) · [SHA-256](artifacts/AndroidAutoSDL-Bridge/v0.14/AndroidAuto-SDL-Bridge-0.14.sha256.txt) · [Full source](artifacts/AndroidAutoSDL-Bridge/v0.14/fonte.zip) · [Version notes](artifacts/AndroidAutoSDL-Bridge/v0.14/icone-e-validacao.md)

| Feature | Evidence and limits |
| --- | --- |
| Registration, Android Auto picture and touch | Owner-confirmed on the test PLAY+. |
| Stable video, dedicated UI, DPI/aspect and Bluetooth playback | Confirmed in earlier physical tests; the owner preferred 115% aspect correction. |
| Dedicated microphone selection | Capture and preference persistence checked locally in 0.13; phone/vehicle test pending. |
| Open Headunit icon | Added in 0.14; acceptance and appearance on the PLAY+ require a physical test. |
| Driving-state transitions | Software handling implemented in 0.12; actual authorization while moving is not guaranteed. |
| Other head units | Not validated. |

An SDL acknowledgement alone does not prove that an image or icon appeared. Version notes distinguish local checks from vehicle tests.

### Compatibility identity

The central accepted the experimental identity **TomTom / App ID 3610601450**. An earlier build changed both name and icon and failed to appear; restoring the identity restored registration. Those changes were not isolated, so the exact acceptance rule is not fully known.

The central may still list this bridge as **TomTom**. This is a compatibility constraint, not a claim of affiliation or approval by Toyota, TomTom, Google or the SDL Consortium. Avoid running the original TomTom and the bridge simultaneously with that identity. Version 0.14 changes only artwork and uses session-scoped uploads; central-side caching may still affect the icon.

## Getting started

1. Install the APK on the phone. Package: `br.local.aasdlbridge`. Current builds can update the preceding bridge installation with the same signing certificate.
2. Enable Android Auto developer settings and start **Headunit Server**, required by the Self Mode flow used here.
3. Connect to the PLAY+ USB port and grant accessory access.
4. Open **SDL** in the app, connect and open its registered application on the head unit as needed. Start video when authorized, or enable automatic video start.
5. Adjust DPI/aspect while video is stopped. Preferences are saved.
6. With media-only Bluetooth, select **Celular** and grant microphone access.

To test the icon, leave **Enviar ícone à central** enabled and reconnect SDL. If there is a regression, disable the option and reconnect; cached artwork may remain. [Version 0.13](artifacts/AndroidAutoSDL-Bridge/v0.13) remains available.

Configure and perform manual phone-side tests while parked. Streaming authorization depends on the head unit.

## Repository layout and building

This repository stores **versioned delivery artifacts**, rather than a conventional source tree at its root:

- [Bridge artifacts](artifacts/AndroidAutoSDL-Bridge/): APKs, hashes, full source snapshots, patches and version notes.
- [SDL Probe artifacts](artifacts/SDL-Probe/): diagnostic prototypes used to establish registration, video and touch compatibility.
- [Analysis](docs/analysis/) and [test records](docs/testing/): protocol evidence, physical results and limitations.

Extract a version's `fonte.zip`, configure the Android SDK and use **JDK 21** with the included wrapper:

```powershell
.\gradlew.bat :app:assembleGithubDebug --no-daemon
```

The source specifies compile SDK 36, NDK 29.0.14206865 and CMake 3.22.1. Each patch is a full diff against the upstream snapshot above: use the full archive **or** apply the patch to that exact snapshot, not both. Test requirements and outcomes are recorded per version.

Changes go directly to **main**, preserving earlier artifact versions.

## Credits and license

The main application and Android Auto receiver come from [Open Headunit](https://github.com/andreknieriem/open-headunit) and its contributors, including the original work credited upstream. SDL integration uses the [SmartDeviceLink Android library](https://github.com/smartdevicelink/sdl_android). The launcher artwork comes from the same Open Headunit snapshot.

The derived application remains under **AGPL-3.0**. See [LICENSE](LICENSE) and the [inherited copyright notice](COPYRIGHT_MICHAEL_REID_GPLv3AFFERO.txt). Source archives preserve upstream notices; dependencies retain their respective licenses.
